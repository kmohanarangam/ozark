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
package org.mvcspec.ozark.binding;

import javax.enterprise.inject.Vetoed;
import javax.mvc.binding.BindingError;
import javax.mvc.binding.BindingResult;
import javax.mvc.binding.ValidationError;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

/**
 * Implementation for {@link javax.mvc.binding.BindingResult} interface.
 *
 * @author Santiago Pericas-Geertsen
 * @author Christian Kaltepoth
 */
@Vetoed // produced by BindingResultManager
public class BindingResultImpl implements BindingResult {

    private final Set<BindingError> bindingErrors = new LinkedHashSet<>();

    private final Set<ValidationError> validationErrors = new LinkedHashSet<>();

    private boolean consumed;

    @Override
    public boolean isFailed() {
        this.consumed = true;
        return validationErrors.size() > 0 || bindingErrors.size() > 0;
    }

    @Override
    public List<String> getAllMessages() {
        this.consumed = true;
        final List<String> result = new ArrayList<>();
        bindingErrors.forEach(error -> result.add(error.getMessage()));
        validationErrors.forEach(violation -> result.add(violation.getMessage()));
        return Collections.unmodifiableList(result);
    }

    @Override
    public Set<BindingError> getAllBindingErrors() {
        this.consumed = true;
        return Collections.unmodifiableSet(bindingErrors);
    }

    @Override
    public BindingError getBindingError(String param) {
        this.consumed = true;
        for (BindingError error : bindingErrors) {
            if (param.equals(error.getParamName())) {
                return error;
            }
        }
        return null;
    }

    @Override
    public Set<ValidationError> getAllValidationErrors() {
        this.consumed = true;
        return Collections.unmodifiableSet(validationErrors);
    }

    @Override
    public Set<ValidationError> getValidationErrors(String param) {
        this.consumed = true;
        return validationErrors.stream()
                .filter(ve -> Objects.equals(ve.getParamName(), param))
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    @Override
    public ValidationError getValidationError(String param) {
        this.consumed = true;
        return validationErrors.stream()
                .filter(ve -> Objects.equals(ve.getParamName(), param))
                .findFirst().orElse(null);
    }

    public void addValidationErrors(Set<ValidationError> validationErrors) {
        this.validationErrors.addAll(validationErrors);
    }

    public void addBindingError(BindingError bindingError) {
        this.bindingErrors.add(bindingError);
    }

    public boolean hasUnconsumedErrors() {
        return !consumed && (!bindingErrors.isEmpty() || !validationErrors.isEmpty());
    }

}
