insert into localized_news (id, news_id, title, brief, content, language_id, date)
values (nextval('localized_news_sequence'), 1, 'first news', 'first news brief', 'first news contentfffffffffffff', 1,
        '2022-05-06'),
       (nextval('localized_news_sequence'), 1, 'первые новости', 'коротко новость 1', 'новость 1 содержание', 2,
        '2022-05-06'),
       (nextval('localized_news_sequence'), 2, 'second', 'secondnews brief', 'second news contentfffffffffffff', 1,
        '2022-05-07'),
       (nextval('localized_news_sequence'), 2, 'вторые новости', 'коротко новость 2',
        'новость 2 содержание аааааааааааааааааа', 2, '2022-05-07');