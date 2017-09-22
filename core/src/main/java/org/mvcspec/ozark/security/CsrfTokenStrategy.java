/*
 * Copyright © 2017 Ivar Grimstad (ivar.grimstad@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mvcspec.ozark.security;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * SPI to support different ways to store the CSRf token
 *
 * @author Christian Kaltepoth
 */
public interface CsrfTokenStrategy {

    Optional<CsrfToken> getToken(HttpServletRequest request);

    void storeToken(HttpServletRequest request, CsrfToken token);

}