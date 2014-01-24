--
-- Create user for local AMQ broker with password 'ch0xpa33'
--
create role chox_amq PASSWORD 'ch0xpa33' NOSUPERUSER NOCREATEDB NOCREATEROLE LOGIN;

--
-- Create database and user for standalone ActiveMQ instance
--
create database activemq;
create role activemq PASSWORD 'act1vemq' NOSUPERUSER NOCREATEDB NOCREATEROLE LOGIN;
alter database activemq owner to activemq;
