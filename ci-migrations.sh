#!/bin/bash

psql -U postgres -d postgres -c 'CREATE DATABASE IF NOT EXISTS opensrp_test; CREATE DATABASE IF NOT EXISTS dbconverter_test;'
