/*
 * Copyright (c) 2016-2016 Bas van den Boom 'Z3r0byte'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
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

package net.ilexiconn.magister.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.ilexiconn.magister.container.SingleStudyGuide;
import net.ilexiconn.magister.container.StudyGuideItem;
import net.ilexiconn.magister.util.GsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingleStudyGuideAdapter extends TypeAdapter<SingleStudyGuide> {
    public Gson gson = GsonUtil.getGson();

    @Override
    public void write(JsonWriter out, SingleStudyGuide value) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public SingleStudyGuide read(JsonReader in) throws IOException {
        JsonObject object = gson.getAdapter(JsonElement.class).read(in).getAsJsonObject();
        SingleStudyGuide studyGuide = gson.fromJson(object, SingleStudyGuide.class);
        JsonArray array = object.get("Onderdelen").getAsJsonObject().get("Items").getAsJsonArray();
        List<StudyGuideItem> studyGuideItemList = new ArrayList<StudyGuideItem>();
        for (JsonElement element : array) {
            studyGuideItemList.add(gson.fromJson(element, StudyGuideItem.class));
        }
        studyGuide.items = studyGuideItemList.toArray(new StudyGuideItem[studyGuideItemList.size()]);
        return studyGuide;
    }
}
