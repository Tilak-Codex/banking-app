import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8081",
  realm: "banking",
  clientId: "banking-app",
});

export default keycloak;