CREATE TABLE IF NOT EXISTS anomalies(
                                          id VARCHAR(64) NOT NULL PRIMARY KEY,
                                          room_id VARCHAR(20) NOT NULL,
                                          thermometer_id VARCHAR(20) NOT NULL,
                                          temperature DECIMAL(10,2),
                                          anomaly_time TIMESTAMP  NOT NULL
);