/*
 * Copyright (C) 2018 Raul Hernandez Lopez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.raulh82vlc.flickrj.feed.domain.mapper;

import com.raulh82vlc.flickrj.common.mapping.Mapper;

import java.util.List;

/**
 * Tags mapper to convert the list into plane strings
 * @author Raul Hernandez Lopez.
 */

public class TagsMapper implements Mapper<List<String>, String> {
    @Override
    public String map(List<String> input) {
        int size = input.size();
        StringBuilder stringBuilder = new StringBuilder();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                stringBuilder.append("#" + input.get(i));
                if (i != size - 1) {
                    stringBuilder.append(" ");
                }
            }
        }
        return stringBuilder.toString();
    }
}
