# 📝 FoodCart Microservices — TODO List

## 🔐 1. Authentication & Authorization (Keycloak)
- [x] Setup Keycloak with `foodcart` realm
- [x] Create roles: `USER`, `ADMIN`, `DELIVERY`
- [x] Create users (`ankit`, `admin`, `dev`) and assign roles
- [x] Create clients (`frontend`, `order-service`, `product-service`, etc.)
- [x] Test token issuance & JWT role claims
- [ ] Integrate token validation in Spring Boot apps
- [ ] Secure endpoints using `@PreAuthorize`

## 🛠️ 2. Microservice Development
### Auth-Service
- [x] Register/login endpoints
- [x] Return JWT or forward to Keycloak
- [ ] `/me` endpoint for profile fetch (secured)

### Product-Service
- [x] List products (`GET /products`)
- [x] Add/delete products (ADMIN only)
- [ ] Role-based protection on endpoints

### Cart-Service
- [ ] Add to cart
- [ ] Remove from cart
- [ ] View cart (user-specific)
- [ ] Secure all endpoints to `USER` role only

### Order-Service
- [ ] Place order (calls cart + product)
- [ ] View order history
- [ ] Admin can view all orders

## ☁️ 3. Infrastructure & Config
- [x] Docker + Postgres + Keycloak with PVC
- [x] Create Keycloak realm and clients
- [ ] Set up Spring Cloud Config Server
- [ ] Configure all services to read from central config
- [ ] Use Git-backed config repo (optional)

## 🚪 4. API Gateway
- [ ] Spring Cloud Gateway routing to all services
- [ ] Secure gateway with JWT validation
- [ ] Rate limiting and path filtering (optional)

## 🛠️ 5. CI/CD & DevOps
- [ ] Containerize all services (Dockerfiles)
- [ ] Helm chart for each service
- [ ] Deploy to Minikube / Kind cluster
- [ ] Add central logging (ELK stack) *(optional)*
- [ ] Prometheus + Grafana setup *(optional)*

## 📦 6. Future: Delivery-Service
- [ ] Create delivery-service (after core services are ready)
- [ ] Role: `DELIVERY`
- [ ] Endpoints: `/deliveries`, `/deliveries/{id}/status`
- [ ] Protect endpoints with `hasRole('DELIVERY')`
