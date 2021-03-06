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

package net.ilexiconn.magister.container;

import com.google.gson.annotations.SerializedName;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.sub.Attachment;
import net.ilexiconn.magister.container.sub.Link;
import net.ilexiconn.magister.util.DateUtil;
import net.ilexiconn.magister.util.GsonUtil;
import net.ilexiconn.magister.util.HttpUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SingleMessage implements Serializable {
    @SerializedName("Inhoud")
    public String content;

    /* TODO: KopieOntvangers */

    /* TODO: Bijlagen */

    @SerializedName("Id")
    public int id = 0;

    @SerializedName("MapId")
    public int mapId = 0;

    @SerializedName("MapTitel")
    public String mapTitle = "";

    @SerializedName("Links")
    public Link[] links;

    @SerializedName("Onderwerp")
    public String topic;

    @SerializedName("Afzender")
    public Contact sender = new Contact();

    @SerializedName("IngekortBericht")
    public String shortMessage = "";

    @SerializedName("Ontvangers")
    public Contact[] recipients;

    @SerializedName("VerstuurdOp")
    public String sentOn;

    @SerializedName("IsGelezen")
    public boolean isRead = false;

    @SerializedName("Status")
    public int status = 0;

    @SerializedName("HeeftPrioriteit")
    public boolean hasPriority = false;

    @SerializedName("HeeftBijlagen")
    public boolean hasAttachments = false;

    @SerializedName("Soort")
    public int type = 1;

    @SerializedName("Bijlagen")
    private Attachment[] attachments;

    public SingleMessage(String topic, String content, Contact[] recipients) throws ParseException, IOException {
        this(null, topic, content, recipients, new File[]{});
    }

    public SingleMessage(Magister m, String topic, String content, Contact[] recipients, File attachment) throws ParseException, IOException {
        this(m, topic, content, recipients, new File[]{attachment});
    }

    public SingleMessage(Magister m, String topic, String content, Contact[] recipients, File[] attachments) throws ParseException, IOException {
        if (!(attachments == null || attachments.length <= 0)) {
            this.hasAttachments = true;
            List<Attachment> list = new ArrayList<Attachment>();
            for (File f : attachments) {
                list.add(getAttachmentFromFile(m, f));
            }
            this.attachments = new Attachment[list.size()];
            this.attachments = list.toArray(this.attachments);
        }
        if (recipients == null || recipients.length <= 0) {
            throw new InvalidParameterException("Recipients must not be null and have a length higher than 0");
        }
        this.topic = topic;
        this.content = content;
        this.recipients = recipients;
        sentOn = DateUtil.dateToString(new Date());
    }

    private static Attachment getAttachmentFromFile(Magister m, File f) throws IOException, ParseException {
        if (f.isDirectory()) {
            throw new FileNotFoundException("File must not be an Directory");
        }
        Attachment a = new Attachment();
        a.fileName = f.getName();
        a.contentType = URLConnection.guessContentTypeFromStream(new BufferedInputStream(new FileInputStream(f)));
        a.id = 0;
        a.sourceType = 0;
        a.uploadDate = DateUtil.dateToString(new Date());
        a.fileSizeInBytes = f.length();
        String response = HttpUtil.convertInputStreamReaderToString(HttpUtil.httpPostFile(m, f));
        a.uniqueId = GsonUtil.getFromJson(response, "Value").getAsString();
        return a;
    }

    public URL[] getAttachmentsUrls(Magister magister) throws MalformedURLException {
        if (!hasAttachments) {
            return null;
        }
        List<URL> urls = new ArrayList<URL>();
        for (Attachment attachment : attachments) {
            for (Link link : attachment.links) {
                if ("Self".equalsIgnoreCase(link.rel)) {
                    urls.add(new URL(magister.school.url + link.href));
                }
            }
        }
        if (urls.size() == 0) {
            return null;
        }
        return urls.toArray(new URL[urls.size()]);
    }
}
