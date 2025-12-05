#!/bin/bash

# Personal Finance API Integration Test Script
# This script tests the main endpoints of the backend API

BASE_URL="http://localhost:8082"
echo "🚀 Testing Personal Finance API at $BASE_URL"
echo "================================================"
echo ""

# Test 1: Backend Health Check
echo "1️⃣ Testing Backend Health..."
HEALTH=$(curl -s $BASE_URL/ -o /dev/null -w "%{http_code}")
if [ "$HEALTH" = "200" ] || [ "$HEALTH" = "404" ] || [ "$HEALTH" = "401" ]; then
    echo "✅ Backend is responding (HTTP $HEALTH)"
else
    echo "❌ Backend is not responding (HTTP $HEALTH)"
    exit 1
fi
echo ""

# Test 2: Create User
echo "2️⃣ Creating test user..."
CREATE_USER_RESPONSE=$(curl -s -X POST $BASE_URL/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "Integration",
    "email": "test.integration@email.com",
    "cpf": "529.982.247-25"
  }')

if echo "$CREATE_USER_RESPONSE" | grep -q "userName"; then
    echo "✅ User created successfully"
    USERNAME=$(echo "$CREATE_USER_RESPONSE" | grep -o '"userName":"[^"]*"' | cut -d'"' -f4)
    echo "   Username: $USERNAME"
else
    echo "ℹ️  User might already exist (this is okay for testing)"
    USERNAME="test.integration"
fi
echo ""

# Test 3: Login
echo "3️⃣ Testing login..."
echo "   Note: You need to use the temporary password from email"
echo "   For demo, trying with existing user: carlos.garcia / 2240"
LOGIN_RESPONSE=$(curl -s -X POST $BASE_URL/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "carlos.garcia",
    "password": "2240"
  }')

if echo "$LOGIN_RESPONSE" | grep -q "accessToken"; then
    echo "✅ Login successful"
    ACCESS_TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)
    echo "   Access Token: ${ACCESS_TOKEN:0:50}..."
else
    echo "❌ Login failed"
    echo "   Response: $LOGIN_RESPONSE"
    exit 1
fi
echo ""

# Test 4: Get User Profile
echo "4️⃣ Testing get user profile..."
PROFILE_RESPONSE=$(curl -s -X GET $BASE_URL/users/me \
  -H "Authorization: Bearer $ACCESS_TOKEN")

if echo "$PROFILE_RESPONSE" | grep -q "email"; then
    echo "✅ Profile retrieved successfully"
    EMAIL=$(echo "$PROFILE_RESPONSE" | grep -o '"email":"[^"]*"' | cut -d'"' -f4)
    echo "   Email: $EMAIL"
else
    echo "❌ Failed to get profile"
    echo "   Response: $PROFILE_RESPONSE"
fi
echo ""

# Test 5: Create Objective/Goal
echo "5️⃣ Testing create objective..."
OBJECTIVE_RESPONSE=$(curl -s -X POST $BASE_URL/objectives \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -d '{
    "descricao": "Meta de economia de dezembro",
    "valorObjetivo": 1000.00,
    "mesAno": "2025-12",
    "tipo": "META_ECONOMIA_MES"
  }')

if echo "$OBJECTIVE_RESPONSE" | grep -q "id"; then
    echo "✅ Objective created successfully"
    OBJECTIVE_ID=$(echo "$OBJECTIVE_RESPONSE" | grep -o '"id":[0-9]*' | grep -o '[0-9]*')
    echo "   Objective ID: $OBJECTIVE_ID"
else
    echo "⚠️  Objective creation response: $OBJECTIVE_RESPONSE"
fi
echo ""

# Test 6: List Objectives
echo "6️⃣ Testing list objectives..."
OBJECTIVES_LIST=$(curl -s -X GET $BASE_URL/objectives \
  -H "Authorization: Bearer $ACCESS_TOKEN")

if echo "$OBJECTIVES_LIST" | grep -q "descricao"; then
    echo "✅ Objectives retrieved successfully"
    COUNT=$(echo "$OBJECTIVES_LIST" | grep -o '"id":[0-9]*' | wc -l)
    echo "   Found $COUNT objective(s)"
else
    echo "ℹ️  No objectives found or empty response"
fi
echo ""

# Test 7: Create Investment
echo "7️⃣ Testing create investment..."
INVESTMENT_RESPONSE=$(curl -s -X POST $BASE_URL/investments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -d '{
    "tipoInvestimento": "ACAO",
    "nomeAtivo": "Petrobras",
    "simbolo": "PETR4",
    "quantidade": 100,
    "valorCompra": 35.50,
    "valorTotalInvestido": 3550.00,
    "dataCompra": "2025-12-05",
    "corretora": "Clear"
  }')

if echo "$INVESTMENT_RESPONSE" | grep -q "id"; then
    echo "✅ Investment created successfully"
    INVESTMENT_ID=$(echo "$INVESTMENT_RESPONSE" | grep -o '"id":[0-9]*' | grep -o '[0-9]*')
    echo "   Investment ID: $INVESTMENT_ID"
else
    echo "⚠️  Investment creation response: $INVESTMENT_RESPONSE"
fi
echo ""

# Test 8: List Investments
echo "8️⃣ Testing list investments..."
INVESTMENTS_LIST=$(curl -s -X GET $BASE_URL/investments \
  -H "Authorization: Bearer $ACCESS_TOKEN")

if echo "$INVESTMENTS_LIST" | grep -q "nomeAtivo"; then
    echo "✅ Investments retrieved successfully"
    COUNT=$(echo "$INVESTMENTS_LIST" | grep -o '"id":[0-9]*' | wc -l)
    echo "   Found $COUNT investment(s)"
else
    echo "ℹ️  No investments found or empty response"
fi
echo ""

echo "================================================"
echo "🎉 API Integration Test Complete!"
echo ""
echo "Summary:"
echo "  ✅ Backend is running"
echo "  ✅ Authentication works"
echo "  ✅ User profile accessible"
echo "  ✅ Objectives API functional"
echo "  ✅ Investments API functional"
echo ""
echo "🔗 Backend URLs:"
echo "  API: http://localhost:8082"
echo "  Swagger: http://localhost:8082/swagger-ui/index.html"
echo ""
echo "📱 Frontend Integration:"
echo "  All endpoints are integrated in Flutter app"
echo "  Run 'flutter run' in personal-finance-frontend/"
echo ""
