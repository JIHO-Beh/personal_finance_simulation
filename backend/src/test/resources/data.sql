INSERT INTO countries (id, country_code, country_name, currency_code) VALUES (1, 'US', '미국', 'USD');
INSERT INTO countries (id, country_code, country_name, currency_code) VALUES (2, 'KR', '한국', 'KRW');

INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) VALUES (1, 0.1150, 0.0765);
INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) VALUES (2, 0.0230, 0.0940);