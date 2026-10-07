import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8080/auth",
  realm: "banking",
  clientId: "banking-app",
});

export default keycloak;