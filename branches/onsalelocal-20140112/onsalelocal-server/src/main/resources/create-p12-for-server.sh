#!/bin/bash

openssl x509 -in 20131223.cer -inform DER -out developer_identity.pem -outform PEM
openssl pkcs12 -nocerts -in 20131223.p12 -out mykey.pem
openssl pkcs12 -export -inkey mykey.pem -in developer_identity.pem -out shopplus_for_dev_srv.p12

