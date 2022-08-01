package org.zhumagulova.springbootnewsportal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class LocalizedNewsDto implements Serializable {

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String title;
    private String brief;
    private String content;

    public LocalizedNewsDto(LocalDate date, String title, String brief, String content) {
        this.date = date;
        this.title = title;
        this.brief = brief;
        this.content = content;
    }

    public LocalizedNews toLocalizedNews(LocalizedNewsDto newsDto) {
        return LocalizedNews.builder()
                .title(newsDto.getTitle())
                .brief(newsDto.getBrief())
                .content(newsDto.getContent())
                .date(newsDto.getDate())
                .build();
    }

    public static LocalizedNewsDto fromLocalizedNews(LocalizedNews localizedNews) {
        return LocalizedNewsDto.builder()
                .title(localizedNews.getTitle())
                .brief(localizedNews.getBrief())
                .content(localizedNews.getContent())
                .date(localizedNews.getDate())
                .build();
    }
}
