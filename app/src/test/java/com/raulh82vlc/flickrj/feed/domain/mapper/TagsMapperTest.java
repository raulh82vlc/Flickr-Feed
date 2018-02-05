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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tags Mapper to check the nice # before the word
 * @author Raul Hernandez Lopez.
 */
public class TagsMapperTest {
    @Test
    public void map() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("Spain");
        tags.add("Italy");
        tags.add("UK");
        String tagsMapped = new TagsMapper().map(tags);
        assertEquals("#Spain #Italy #UK", tagsMapped);
    }

    @Test
    public void mapNoTags() throws Exception {
        List<String> tags = new ArrayList<>();
        String tagsMapped = new TagsMapper().map(tags);
        assertEquals("", tagsMapped);
    }
}