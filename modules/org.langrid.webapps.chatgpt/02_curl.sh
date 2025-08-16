curl https://api.openai.com/v1/responses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer APIKEY" \
  -d '{
    "model": "gpt-5-nano",
    "input": "Write a short bedtime story about a unicorn."
  }'
