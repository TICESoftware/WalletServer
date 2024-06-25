#!/bin/bash
ENV_FILE=.env
EXAMPLE_FILE=example.env

if [ ! -f "$ENV_FILE" ]; then
  echo "$ENV_FILE does not exist"
  exit 1
fi

exit_code=0
for key in $(grep -v '^#\|^[[:space:]]*$' "$EXAMPLE_FILE" | cut -d '=' -f 1); do
  if ! grep -q "^${key}=" "$ENV_FILE"; then
    echo "The key $key does not exist in $ENV_FILE"
    exit_code=1
  fi
  if grep -q "^${key}=[[:space:]]*$" "$ENV_FILE"; then
    echo "The key $key is empty in $ENV_FILE"
    exit_code=1
  fi
done

if [ "$exit_code" == 0 ]; then
  echo "All variables in $EXAMPLE_FILE exist in $ENV_FILE"
fi

exit $exit_code
