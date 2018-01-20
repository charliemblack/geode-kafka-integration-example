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

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

import java.util.UUID;

public class Driver {
    public static void main(String[] args) {
        ClientCache clientCache = new ClientCacheFactory()
                .addPoolLocator("localhost", 10334)
                .setPdxSerializer(new ReflectionBasedAutoSerializer("example.geode.kafka.*"))
                .setPdxReadSerialized(false)
                .set("log-level", "warning")
                .create();
        Region<String, Customer> customerRegion = clientCache.<String, Customer>createClientRegionFactory(ClientRegionShortcut.PROXY).create("test");

        Fairy fairy = Fairy.create();
        for (int i = 0; i < 10; i++) {
            Person person = fairy.person();
            Customer customer = Customer.builder()
                    .firstName(person.getFirstName())
                    .middleName(person.getMiddleName())
                    .lastName(person.getLastName())
                    .email(person.getEmail())
                    .username(person.getUsername())
                    .passportNumber(person.getPassportNumber())
                    .password(person.getPassword())
                    .telephoneNumber(person.getTelephoneNumber())
                    .dateOfBirth(person.getDateOfBirth().toString())
                    .age(person.getAge())
                    .companyEmail(person.getCompanyEmail())
                    .nationalIdentificationNumber(person.getNationalIdentificationNumber())
                    .nationalIdentityCardNumber(person.getNationalIdentityCardNumber())
                    .passportNumber(person.getPassportNumber())
                    .guid(UUID.randomUUID().toString()).build();

            System.out.println("new customer.getGuid() = " + customer.getGuid());

            customerRegion.put(customer.getGuid(), customer);
        }
    }
}
