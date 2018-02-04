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

package com.raulh82vlc.flickrj.data.datasource.network.response;

import com.google.gson.Gson;
import com.raulh82vlc.flickrj.data.FileReaderHelper;
import com.raulh82vlc.flickrj.data.datasource.network.model.FeedApiModel;
import com.raulh82vlc.flickrj.data.datasource.network.model.FeedItemApiModel;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Response handler to check all little steps of the workflow filtering work nicely
 * @author Raul Hernandez Lopez.
 */
public class ResponseHandlerTest {

    private static final String RAW_FEED_JSON = "raw/feed.json";
    private static final MediaType JSON_UTF = MediaType.parse(FileReaderHelper.APPLICATION_JSON);
    private ResponseHandler responseHandler;

    @Before
    public void setUp() {
        responseHandler = new ResponseHandler(new Gson());
    }

    @Test
    public void extractJSONFromResponse() throws Exception {
        String json = responseHandler.extractJSONFromResponse(getMockFeedResponse());
        assertFalse(responseHandler.hasFeedFormat(json));
    }

    @Test
    public void deserializeFeedJSON() throws Exception {
        FeedApiModel apiModel = responseHandler
                .deserializeFeedJSON(responseHandler
                        .extractJSONFromResponse(getMockFeedResponse()));
        assertEquals("2018-01-25T19:40:46Z", apiModel.getModified());
    }

    @Test
    public void hasFeedFormat() throws Exception {
        String content = "jsonFlickrFeed({\n"
                + "  \"title\": \"U";
        assertTrue(responseHandler.hasFeedFormat(content));
    }

    @Test
    public void hasNoApiFailure() throws Exception {
        assertTrue(responseHandler.hasNoApiFailure("ok"));
    }

    @Test
    public void hasNoErrorResponse() throws Exception {
        assertTrue(responseHandler.hasNoErrorResponse(getMockResult()));
    }

    @Test
    public void getStringContent() throws Exception {
        String content = responseHandler.getStringContent(getMockResult());
        assertTrue(content.length() > 0);
        assertTrue(responseHandler.hasFeedFormat(content));
    }

    @Test
    public void returnListOfItems() throws Exception {
        List<FeedItemApiModel> listOfItems = responseHandler.returnListOfItems(
                responseHandler
                        .deserializeFeedJSON(
                                responseHandler.extractJSONFromResponse(getMockFeedResponse())));
        assertEquals("Bookshelf", listOfItems.get(0).getTitle());
        assertEquals("Emperor @ Tons Of Rock 2017-54.jpg", listOfItems.get(1).getTitle());
    }

    private Result<ResponseBody> getMockResult() {
        return
                Result.response(
                        Response.success(
                                ResponseBody.create(JSON_UTF, getMockFeedResponse())));
    }

    private String getMockFeedResponse() {
        try {
            return FileReaderHelper.readTextFromFile(this.getClass()
                    .getClassLoader().getResourceAsStream(RAW_FEED_JSON));
        } catch (Exception e) {
            return "{}";
        }
    }
}