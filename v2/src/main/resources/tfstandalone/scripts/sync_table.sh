#!/bin/bash

mysql_host=$1
mysql_user=$2
mysql_password=$3
database=$4
table=$5
schema_file=$6
temp_database="mig_db_${database}_t_${table}"

if `mysql -u$mysql_user -p$mysql_password -h $mysql_host -e "use $database; show create table $table;" &> /dev/null`;
    then
        mkdir -p /tmp/$mysql_host
        mysql -u$mysql_user -p$mysql_password -h $mysql_host -e "drop database if exists $temp_database; create database $temp_database;"
        mysql -u$mysql_user -p$mysql_password -h $mysql_host $temp_database < $schema_file
        mysqldiff --server1=$mysql_user:$mysql_password@$mysql_host ${database}.${table}:${temp_database}.${table} --difftype=sql -q -c > /tmp/${mysql_host}/${temp_database}.sql
        mysql -u$mysql_user -p$mysql_password -h $mysql_host $database < /tmp/${mysql_host}/${temp_database}.sql
        mysql -u$mysql_user -p$mysql_password -h $mysql_host -e "drop database if exists $temp_database;"
    else
        mysql -u$mysql_user -p$mysql_password -h $mysql_host $database < $schema_file
fi