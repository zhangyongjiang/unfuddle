#!/bin/bash

name=nextshopper-dev-20140201
openssl x509 -in $name.cer -inform DER -out temp_identity.pem -outform PEM
openssl pkcs12 -nocerts -in $name.p12 -out temp_key.pem
openssl pkcs12 -export -inkey temp_key.pem -in temp_identity.pem -out $name-for-srv.p12

