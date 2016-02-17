#!/bin/bash

echo -n "Enter your oracle username:"
read username
echo -n "Enter your oracle password:"
read password

echo "User: $username Password: $password"

echo "Loading data into STUDENT table....."
/opt/oracle/app/csoracle/product/12.1.0/dbhome_1/bin/sqlldr userid=$username/$password control=student.ctl
echo "Loading data into COURSE table....."
/opt/oracle/app/csoracle/product/12.1.0/dbhome_1/bin/sqlldr userid=$username/$password control=course.ctl
echo "Loading data into SECTION table....."
/opt/oracle/app/csoracle/product/12.1.0/dbhome_1/bin/sqlldr userid=$username/$password control=section.ctl
echo "Loading data into GRADE_REPORT table....."
/opt/oracle/app/csoracle/product/12.1.0/dbhome_1/bin/sqlldr userid=$username/$password control=grade_report.ctl
echo "Loading data into PREREQUISITE table....."
/opt/oracle/app/csoracle/product/12.1.0/dbhome_1/bin/sqlldr userid=$username/$password control=prerequisite.ctl
