/*
 * Copyright  2018 Charlie Black
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package example.geode.kafka;


import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;
import org.apache.geode.internal.logging.LogService;
import org.apache.geode.pdx.JSONFormatter;
import org.apache.geode.pdx.PdxInstance;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Properties;

public class KafkaAsyncEventListener implements AsyncEventListener, Declarable {
    private static Logger logger = LogService.getLogger();

    private KafkaProducer<String, String> producer;

    @Override
    public boolean processEvents(List<AsyncEvent> events) {

        producer.beginTransaction();
        try {

            for (AsyncEvent asyncEvent : events) {
                PdxInstance pdxInstance = (PdxInstance) asyncEvent.getDeserializedValue();
                String topic = asyncEvent.getRegion().getName();
                String key = (String) asyncEvent.getKey();
                String value = JSONFormatter.toJSON(pdxInstance);
                producer.send(new ProducerRecord<>(topic, key, value));
            }
            producer.commitTransaction();
        } catch (Exception e) {
            logger.error("Could not send data to kafka.", e);
            producer.abortTransaction();
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        producer.close();
    }

    @Override
    public void init(Properties props) {
        Properties kafkaProps = new Properties();

        kafkaProps.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaProps.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        kafkaProps.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        kafkaProps.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        kafkaProps.setProperty(ProducerConfig.LINGER_MS_CONFIG, "0");
        kafkaProps.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, getClass().getName());

        kafkaProps.putAll(props); //overwrite anything from the command line properties

        if (logger.isInfoEnabled()) {
            StringBuffer buffer = new StringBuffer("The configuration for Kafka is:\n");
            kafkaProps.entrySet().forEach(e -> {
                buffer.append("\t " + e.getKey() + " = " + e.getValue() + "\n");
            });
            logger.info(buffer.toString());
        }
        //get the current context class loader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(StringSerializer.class.getClassLoader());
        producer = new KafkaProducer<>(kafkaProps);
        producer.initTransactions();
        //restore the class loader
        Thread.currentThread().setContextClassLoader(classLoader);
    }
}
