CREATE TABLE countries (
    id BIGSERIAL PRIMARY KEY,
    country_code VARCHAR(2) NOT NULL UNIQUE,
    country_name VARCHAR(255) NOT NULL,
    currency_code VARCHAR(3) NOT NULL
);


CREATE TABLE tax_rate (
    id BIGSERIAL PRIMARY KEY,
    country_id BIGINT NOT NULL UNIQUE,
    income_tax_rate DECIMAL(5, 4) NOT NULL,
    social_security_rate DECIMAL(5, 4) NOT NULL,
    FOREIGN KEY (country_id) REFERENCES countries (id)
);


CREATE TABLE exchange_rate (
    id BIGSERIAL PRIMARY KEY,
    currency_code VARCHAR(3) NOT NULL UNIQUE,
    rate_to_usd DECIMAL(15, 6) NOT NULL,
    last_updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('US', '미국', 'USD') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.1150, 0.0765 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;
WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('JP', '일본', 'JPY') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.0980, 0.1470 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;
WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('CN', '중국', 'CNY') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.0850, 0.1050 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;
WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('CA', '캐나다', 'CAD') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.1420, 0.0763 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;
WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('AU', '호주', 'AUD') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.1850, 0.0000 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;
WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('VN', '베트남', 'VND') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.1250, 0.1050 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;
WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('DE', '독일', 'EUR') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.1890, 0.1960 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;
WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('SG', '싱가포르', 'SGD') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.0380, 0.2000 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;
WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('GB', '영국', 'GBP') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.1550, 0.1200 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;
WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('NZ', '뉴질랜드', 'NZD') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.1640, 0.0000 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;
WITH new_country AS (INSERT INTO countries (country_code, country_name, currency_code) VALUES ('KR', '한국', 'KRW') ON CONFLICT (country_code) DO NOTHING RETURNING id) INSERT INTO tax_rate (country_id, income_tax_rate, social_security_rate) SELECT id, 0.0230, 0.0940 FROM new_country ON CONFLICT (country_id) DO UPDATE SET income_tax_rate = EXCLUDED.income_tax_rate, social_security_rate = EXCLUDED.social_security_rate;

INSERT INTO exchange_rate (currency_code, rate_to_usd) VALUES ('JPY', 157.0) ON CONFLICT (currency_code) DO NOTHING;
INSERT INTO exchange_rate (currency_code, rate_to_usd) VALUES ('CNY', 7.25) ON CONFLICT (currency_code) DO NOTHING;
INSERT INTO exchange_rate (currency_code, rate_to_usd) VALUES ('CAD', 1.37) ON CONFLICT (currency_code) DO NOTHING;
INSERT INTO exchange_rate (currency_code, rate_to_usd) VALUES ('AUD', 1.5) ON CONFLICT (currency_code) DO NOTHING;
INSERT INTO exchange_rate (currency_code, rate_to_usd) VALUES ('VND', 25400.0) ON CONFLICT (currency_code) DO NOTHING;
INSERT INTO exchange_rate (currency_code, rate_to_usd) VALUES ('EUR', 0.92) ON CONFLICT (currency_code) DO NOTHING;
INSERT INTO exchange_rate (currency_code, rate_to_usd) VALUES ('SGD', 1.35) ON CONFLICT (currency_code) DO NOTHING;
INSERT INTO exchange_rate (currency_code, rate_to_usd) VALUES ('GBP', 0.78) ON CONFLICT (currency_code) DO NOTHING;
INSERT INTO exchange_rate (currency_code, rate_to_usd) VALUES ('NZD', 1.63) ON CONFLICT (currency_code) DO NOTHING;
INSERT INTO exchange_rate (currency_code, rate_to_usd) VALUES ('KRW', 1380.00) ON CONFLICT (currency_code) DO NOTHING;