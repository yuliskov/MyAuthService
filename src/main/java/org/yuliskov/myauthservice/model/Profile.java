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

package org.yuliskov.myauthservice.model;

import lombok.*;
import java.util.Date;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    private String displayName;
    private String profilePictureUrl;
    private Date birthday;
    private Set<Address> addresses;
}
