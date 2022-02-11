/*
 * Copyright 2022 Yuriy Lyskov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yuliskov.myauthservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@Component
public class JwtConfig {

    @Value("${security.jwt.uri:}")
    private String Uri;

    @Value("${security.jwt.header:}")
    private String header;

    @Value("${security.jwt.prefix:}")
    private String prefix;

    @Value("${security.jwt.expiration:1234}")
    private int expiration;

    @Value("${security.jwt.secret:}")
    private String secret;
}
