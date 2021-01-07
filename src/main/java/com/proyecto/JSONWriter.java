package com.proyecto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONWriter {

    String nombre;
    String config = "{\n" +
            "  \"id\" : \"quarkus-realm\",\n" +
            "  \"realm\" : \"quarkus-realm\",\n" +
            "  \"notBefore\" : 0,\n" +
            "  \"revokeRefreshToken\" : false,\n" +
            "  \"refreshTokenMaxReuse\" : 0,\n" +
            "  \"accessTokenLifespan\" : 300,\n" +
            "  \"accessTokenLifespanForImplicitFlow\" : 900,\n" +
            "  \"ssoSessionIdleTimeout\" : 1800,\n" +
            "  \"ssoSessionMaxLifespan\" : 36000,\n" +
            "  \"ssoSessionIdleTimeoutRememberMe\" : 0,\n" +
            "  \"ssoSessionMaxLifespanRememberMe\" : 0,\n" +
            "  \"offlineSessionIdleTimeout\" : 2592000,\n" +
            "  \"offlineSessionMaxLifespanEnabled\" : false,\n" +
            "  \"offlineSessionMaxLifespan\" : 5184000,\n" +
            "  \"accessCodeLifespan\" : 60,\n" +
            "  \"accessCodeLifespanUserAction\" : 300,\n" +
            "  \"accessCodeLifespanLogin\" : 1800,\n" +
            "  \"actionTokenGeneratedByAdminLifespan\" : 43200,\n" +
            "  \"actionTokenGeneratedByUserLifespan\" : 300,\n" +
            "  \"enabled\" : true,\n" +
            "  \"sslRequired\" : \"external\",\n" +
            "  \"registrationAllowed\" : false,\n" +
            "  \"registrationEmailAsUsername\" : false,\n" +
            "  \"rememberMe\" : false,\n" +
            "  \"verifyEmail\" : false,\n" +
            "  \"loginWithEmailAllowed\" : true,\n" +
            "  \"duplicateEmailsAllowed\" : false,\n" +
            "  \"resetPasswordAllowed\" : false,\n" +
            "  \"editUsernameAllowed\" : false,\n" +
            "  \"bruteForceProtected\" : false,\n" +
            "  \"permanentLockout\" : false,\n" +
            "  \"maxFailureWaitSeconds\" : 900,\n" +
            "  \"minimumQuickLoginWaitSeconds\" : 60,\n" +
            "  \"waitIncrementSeconds\" : 60,\n" +
            "  \"quickLoginCheckMilliSeconds\" : 1000,\n" +
            "  \"maxDeltaTimeSeconds\" : 43200,\n" +
            "  \"failureFactor\" : 30,\n" +
            "  \"roles\" : {\n" +
            "    \"realm\" : [ {\n" +
            "      \"id\" : \"4875a4b5-fcdc-490e-83f0-e5870ac1d95e\",\n" +
            "      \"name\" : \"uma_authorization\",\n" +
            "      \"description\" : \"${role_uma_authorization}\",\n" +
            "      \"composite\" : false,\n" +
            "      \"clientRole\" : false,\n" +
            "      \"containerId\" : \"quarkus-realm\",\n" +
            "      \"attributes\" : { }\n" +
            "    }, {\n" +
            "      \"id\" : \"ba90acf5-2106-43b5-b7f4-7d815e52bb4d\",\n" +
            "      \"name\" : \"user\",\n" +
            "      \"composite\" : false,\n" +
            "      \"clientRole\" : false,\n" +
            "      \"containerId\" : \"quarkus-realm\",\n" +
            "      \"attributes\" : { }\n" +
            "    }, {\n" +
            "      \"id\" : \"ff5dccc5-71e7-4d33-8837-2e31bebba7bc\",\n" +
            "      \"name\" : \"offline_access\",\n" +
            "      \"description\" : \"${role_offline-access}\",\n" +
            "      \"composite\" : false,\n" +
            "      \"clientRole\" : false,\n" +
            "      \"containerId\" : \"quarkus-realm\",\n" +
            "      \"attributes\" : { }\n" +
            "    }, {\n" +
            "      \"id\" : \"c53b0fa3-e342-4821-9a54-8ea27889f3ef\",\n" +
            "      \"name\" : \"admin\",\n" +
            "      \"composite\" : false,\n" +
            "      \"clientRole\" : false,\n" +
            "      \"containerId\" : \"quarkus-realm\",\n" +
            "      \"attributes\" : { }\n" +
            "    } ],\n" +
            "    \"client\" : {\n" +
            "      \"quarkus-client\" : [ ],\n" +
            "      \"realm-management\" : [ {\n" +
            "        \"id\" : \"7cb5c26a-eed3-4c7e-a38c-2c46b28fee20\",\n" +
            "        \"name\" : \"create-client\",\n" +
            "        \"description\" : \"${role_create-client}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"948c986f-52ce-4695-a11b-8403e6ab61ba\",\n" +
            "        \"name\" : \"impersonation\",\n" +
            "        \"description\" : \"${role_impersonation}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"dae52537-3d4f-466e-882d-337ad218cbd3\",\n" +
            "        \"name\" : \"realm-admin\",\n" +
            "        \"description\" : \"${role_realm-admin}\",\n" +
            "        \"composite\" : true,\n" +
            "        \"composites\" : {\n" +
            "          \"client\" : {\n" +
            "            \"realm-management\" : [ \"create-client\", \"impersonation\", \"manage-identity-providers\", \"view-clients\", \"view-identity-providers\", \"view-realm\", \"query-groups\", \"manage-events\", \"view-authorization\", \"view-users\", \"manage-realm\", \"query-realms\", \"query-clients\", \"manage-users\", \"view-events\", \"manage-clients\", \"manage-authorization\", \"query-users\" ]\n" +
            "          }\n" +
            "        },\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"584eedd7-0fe8-44b1-96a3-dfea690b9362\",\n" +
            "        \"name\" : \"manage-identity-providers\",\n" +
            "        \"description\" : \"${role_manage-identity-providers}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"eab3546c-5db9-4465-9837-f363a33b5762\",\n" +
            "        \"name\" : \"view-clients\",\n" +
            "        \"description\" : \"${role_view-clients}\",\n" +
            "        \"composite\" : true,\n" +
            "        \"composites\" : {\n" +
            "          \"client\" : {\n" +
            "            \"realm-management\" : [ \"query-clients\" ]\n" +
            "          }\n" +
            "        },\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"74412e18-7f8e-40b0-b925-0fc690753fc0\",\n" +
            "        \"name\" : \"view-identity-providers\",\n" +
            "        \"description\" : \"${role_view-identity-providers}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"48114796-cd32-421e-ab8d-55e4bed300b5\",\n" +
            "        \"name\" : \"view-realm\",\n" +
            "        \"description\" : \"${role_view-realm}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"889b4bec-84a7-4158-81d1-ab42df8743cb\",\n" +
            "        \"name\" : \"manage-events\",\n" +
            "        \"description\" : \"${role_manage-events}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"389ee808-3b86-4a4e-a0c6-1887ff174bf2\",\n" +
            "        \"name\" : \"query-groups\",\n" +
            "        \"description\" : \"${role_query-groups}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"a74d54f6-883d-4faa-9817-c73182cd893e\",\n" +
            "        \"name\" : \"view-authorization\",\n" +
            "        \"description\" : \"${role_view-authorization}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"89b3e6c0-6e1b-46be-82e3-ad3cc3a15635\",\n" +
            "        \"name\" : \"view-users\",\n" +
            "        \"description\" : \"${role_view-users}\",\n" +
            "        \"composite\" : true,\n" +
            "        \"composites\" : {\n" +
            "          \"client\" : {\n" +
            "            \"realm-management\" : [ \"query-groups\", \"query-users\" ]\n" +
            "          }\n" +
            "        },\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"5ea916a3-4028-4821-8f76-9ae1d476decd\",\n" +
            "        \"name\" : \"manage-realm\",\n" +
            "        \"description\" : \"${role_manage-realm}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"c8d33bb9-62f4-471b-924e-60bc631adaaf\",\n" +
            "        \"name\" : \"query-clients\",\n" +
            "        \"description\" : \"${role_query-clients}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"8df853fc-bd17-494e-85cb-43c40cec2e59\",\n" +
            "        \"name\" : \"query-realms\",\n" +
            "        \"description\" : \"${role_query-realms}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"ff1df86c-2ded-42ce-b2a8-d3ad069aad3d\",\n" +
            "        \"name\" : \"manage-users\",\n" +
            "        \"description\" : \"${role_manage-users}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"799e754b-8858-43b7-bb06-e044a65db5d8\",\n" +
            "        \"name\" : \"view-events\",\n" +
            "        \"description\" : \"${role_view-events}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"2ca8c8f4-a2c0-447b-99ba-096757ba5b8c\",\n" +
            "        \"name\" : \"manage-clients\",\n" +
            "        \"description\" : \"${role_manage-clients}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"e702a177-17e0-472e-8a33-0f94d905510a\",\n" +
            "        \"name\" : \"manage-authorization\",\n" +
            "        \"description\" : \"${role_manage-authorization}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"f5d5670c-5af4-440e-9174-2e91a2d52189\",\n" +
            "        \"name\" : \"query-users\",\n" +
            "        \"description\" : \"${role_query-users}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "        \"attributes\" : { }\n" +
            "      } ],\n" +
            "      \"security-admin-console\" : [ ],\n" +
            "      \"admin-cli\" : [ ],\n" +
            "      \"broker\" : [ {\n" +
            "        \"id\" : \"b6824708-9a54-4de1-b1e3-8c6e82b37236\",\n" +
            "        \"name\" : \"read-token\",\n" +
            "        \"description\" : \"${role_read-token}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"d36113c3-480e-4f46-98b0-95ff47e9f220\",\n" +
            "        \"attributes\" : { }\n" +
            "      } ],\n" +
            "      \"account\" : [ {\n" +
            "        \"id\" : \"0c47191a-cf77-432b-aba0-9588bd061724\",\n" +
            "        \"name\" : \"manage-account-links\",\n" +
            "        \"description\" : \"${role_manage-account-links}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"29d7d4c8-f7a0-48ea-b21f-300f692e6fd9\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"b7609fc3-c307-4bd6-92b3-51e98943dec9\",\n" +
            "        \"name\" : \"manage-account\",\n" +
            "        \"description\" : \"${role_manage-account}\",\n" +
            "        \"composite\" : true,\n" +
            "        \"composites\" : {\n" +
            "          \"client\" : {\n" +
            "            \"account\" : [ \"manage-account-links\" ]\n" +
            "          }\n" +
            "        },\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"29d7d4c8-f7a0-48ea-b21f-300f692e6fd9\",\n" +
            "        \"attributes\" : { }\n" +
            "      }, {\n" +
            "        \"id\" : \"c63accbc-12d2-42fd-9fc6-2d5deeeb248b\",\n" +
            "        \"name\" : \"view-profile\",\n" +
            "        \"description\" : \"${role_view-profile}\",\n" +
            "        \"composite\" : false,\n" +
            "        \"clientRole\" : true,\n" +
            "        \"containerId\" : \"29d7d4c8-f7a0-48ea-b21f-300f692e6fd9\",\n" +
            "        \"attributes\" : { }\n" +
            "      } ]\n" +
            "    }\n" +
            "  },\n" +
            "  \"groups\" : [ ],\n" +
            "  \"defaultRoles\" : [ \"uma_authorization\", \"offline_access\" ],\n" +
            "  \"requiredCredentials\" : [ \"password\" ],\n" +
            "  \"otpPolicyType\" : \"totp\",\n" +
            "  \"otpPolicyAlgorithm\" : \"HmacSHA1\",\n" +
            "  \"otpPolicyInitialCounter\" : 0,\n" +
            "  \"otpPolicyDigits\" : 6,\n" +
            "  \"otpPolicyLookAheadWindow\" : 1,\n" +
            "  \"otpPolicyPeriod\" : 30,\n" +
            "  \"otpSupportedApplications\" : [ \"FreeOTP\", \"Google Authenticator\" ],\n" +
            "  \"users\" : [ {\n" +
            "    \"id\" : \"0773b1e6-ac03-4cec-9e67-56071a72e99d\",\n" +
            "    \"createdTimestamp\" : 1553675800918,\n" +
            "    \"username\" : \"admin\",\n" +
            "    \"enabled\" : true,\n" +
            "    \"totp\" : false,\n" +
            "    \"emailVerified\" : false,\n" +
            "    \"firstName\" : \"Li\",\n" +
            "    \"lastName\" : \"Admin\",\n" +
            "    \"email\" : \"admin@localhost\",\n" +
            "    \"credentials\" : [ {\n" +
            "      \"type\" : \"password\",\n" +
            "      \"hashedSaltedValue\" : \"yOq7XCV421DLy9ePghMhjXzwM1gqd3D0gzcxYvhMDK4jTSHL9+AtOoB6pXJD/Z9GgWlqUPWLcI9LX1m9gta5Zg==\",\n" +
            "      \"salt\" : \"8pS+2QidRTCMyqpf+hYRJg==\",\n" +
            "      \"hashIterations\" : 27500,\n" +
            "      \"counter\" : 0,\n" +
            "      \"algorithm\" : \"pbkdf2-sha256\",\n" +
            "      \"digits\" : 0,\n" +
            "      \"period\" : 0,\n" +
            "      \"createdDate\" : 1553675807758,\n" +
            "      \"config\" : { }\n" +
            "    } ],\n" +
            "    \"disableableCredentialTypes\" : [ \"password\" ],\n" +
            "    \"requiredActions\" : [ ],\n" +
            "    \"realmRoles\" : [ \"uma_authorization\", \"user\", \"offline_access\", \"admin\" ],\n" +
            "    \"clientRoles\" : {\n" +
            "      \"account\" : [ \"manage-account\", \"view-profile\" ]\n" +
            "    },\n" +
            "    \"notBefore\" : 0,\n" +
            "    \"groups\" : [ ]\n" +
            "  }, {\n" +
            "    \"id\" : \"a19b2afc-e96e-4939-82bf-aa4b589de136\",\n" +
            "    \"createdTimestamp\" : 1553266393903,\n" +
            "    \"username\" : \"test\",\n" +
            "    \"enabled\" : true,\n" +
            "    \"totp\" : false,\n" +
            "    \"emailVerified\" : false,\n" +
            "    \"firstName\" : \"Leo\",\n" +
            "    \"lastName\" : \"Tester\",\n" +
            "    \"email\" : \"tester@localhost\",\n" +
            "    \"credentials\" : [ {\n" +
            "      \"type\" : \"password\",\n" +
            "      \"hashedSaltedValue\" : \"olQv2vQTwsXEingIx5RPeIyjgxb/uysmIoCvwgovPGhM7QLIaGp9WcMlAXcKv2P5p9eRFpwT+D8ctuA1Yy1gzg==\",\n" +
            "      \"salt\" : \"FKhAd+XOZDVK+cDdDNOL5g==\",\n" +
            "      \"hashIterations\" : 27500,\n" +
            "      \"counter\" : 0,\n" +
            "      \"algorithm\" : \"pbkdf2-sha256\",\n" +
            "      \"digits\" : 0,\n" +
            "      \"period\" : 0,\n" +
            "      \"createdDate\" : 1553675829010,\n" +
            "      \"config\" : { }\n" +
            "    } ],\n" +
            "    \"disableableCredentialTypes\" : [ \"password\" ],\n" +
            "    \"requiredActions\" : [ ],\n" +
            "    \"realmRoles\" : [ \"uma_authorization\", \"user\", \"offline_access\" ],\n" +
            "    \"clientRoles\" : {\n" +
            "      \"account\" : [ \"manage-account\", \"view-profile\" ]\n" +
            "    },\n" +
            "    \"notBefore\" : 0,\n" +
            "    \"groups\" : [ ]\n" +
            "  } ],\n" +
            "  \"scopeMappings\" : [ {\n" +
            "    \"clientScope\" : \"offline_access\",\n" +
            "    \"roles\" : [ \"offline_access\" ]\n" +
            "  } ],\n" +
            "  \"clients\" : [ {\n" +
            "    \"id\" : \"29d7d4c8-f7a0-48ea-b21f-300f692e6fd9\",\n" +
            "    \"clientId\" : \"account\",\n" +
            "    \"name\" : \"${client_account}\",\n" +
            "    \"baseUrl\" : \"/auth/realms/quarkus-realm/account\",\n" +
            "    \"surrogateAuthRequired\" : false,\n" +
            "    \"enabled\" : true,\n" +
            "    \"clientAuthenticatorType\" : \"client-secret\",\n" +
            "    \"secret\" : \"**********\",\n" +
            "    \"defaultRoles\" : [ \"manage-account\", \"view-profile\" ],\n" +
            "    \"redirectUris\" : [ \"/auth/realms/quarkus-realm/account/*\" ],\n" +
            "    \"webOrigins\" : [ ],\n" +
            "    \"notBefore\" : 0,\n" +
            "    \"bearerOnly\" : false,\n" +
            "    \"consentRequired\" : false,\n" +
            "    \"standardFlowEnabled\" : true,\n" +
            "    \"implicitFlowEnabled\" : false,\n" +
            "    \"directAccessGrantsEnabled\" : false,\n" +
            "    \"serviceAccountsEnabled\" : false,\n" +
            "    \"publicClient\" : false,\n" +
            "    \"frontchannelLogout\" : false,\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : { },\n" +
            "    \"authenticationFlowBindingOverrides\" : { },\n" +
            "    \"fullScopeAllowed\" : false,\n" +
            "    \"nodeReRegistrationTimeout\" : 0,\n" +
            "    \"defaultClientScopes\" : [ \"web-origins\", \"role_list\", \"roles\", \"profile\", \"email\" ],\n" +
            "    \"optionalClientScopes\" : [ \"address\", \"phone\", \"offline_access\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"032c09f3-4fa5-485e-bd07-a3ca1667c38a\",\n" +
            "    \"clientId\" : \"admin-cli\",\n" +
            "    \"name\" : \"${client_admin-cli}\",\n" +
            "    \"surrogateAuthRequired\" : false,\n" +
            "    \"enabled\" : true,\n" +
            "    \"clientAuthenticatorType\" : \"client-secret\",\n" +
            "    \"secret\" : \"**********\",\n" +
            "    \"redirectUris\" : [ ],\n" +
            "    \"webOrigins\" : [ ],\n" +
            "    \"notBefore\" : 0,\n" +
            "    \"bearerOnly\" : false,\n" +
            "    \"consentRequired\" : false,\n" +
            "    \"standardFlowEnabled\" : false,\n" +
            "    \"implicitFlowEnabled\" : false,\n" +
            "    \"directAccessGrantsEnabled\" : true,\n" +
            "    \"serviceAccountsEnabled\" : false,\n" +
            "    \"publicClient\" : true,\n" +
            "    \"frontchannelLogout\" : false,\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : { },\n" +
            "    \"authenticationFlowBindingOverrides\" : { },\n" +
            "    \"fullScopeAllowed\" : false,\n" +
            "    \"nodeReRegistrationTimeout\" : 0,\n" +
            "    \"defaultClientScopes\" : [ \"web-origins\", \"role_list\", \"roles\", \"profile\", \"email\" ],\n" +
            "    \"optionalClientScopes\" : [ \"address\", \"phone\", \"offline_access\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"d36113c3-480e-4f46-98b0-95ff47e9f220\",\n" +
            "    \"clientId\" : \"broker\",\n" +
            "    \"name\" : \"${client_broker}\",\n" +
            "    \"surrogateAuthRequired\" : false,\n" +
            "    \"enabled\" : true,\n" +
            "    \"clientAuthenticatorType\" : \"client-secret\",\n" +
            "    \"secret\" : \"**********\",\n" +
            "    \"redirectUris\" : [ ],\n" +
            "    \"webOrigins\" : [ ],\n" +
            "    \"notBefore\" : 0,\n" +
            "    \"bearerOnly\" : false,\n" +
            "    \"consentRequired\" : false,\n" +
            "    \"standardFlowEnabled\" : true,\n" +
            "    \"implicitFlowEnabled\" : false,\n" +
            "    \"directAccessGrantsEnabled\" : false,\n" +
            "    \"serviceAccountsEnabled\" : false,\n" +
            "    \"publicClient\" : false,\n" +
            "    \"frontchannelLogout\" : false,\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : { },\n" +
            "    \"authenticationFlowBindingOverrides\" : { },\n" +
            "    \"fullScopeAllowed\" : false,\n" +
            "    \"nodeReRegistrationTimeout\" : 0,\n" +
            "    \"defaultClientScopes\" : [ \"web-origins\", \"role_list\", \"roles\", \"profile\", \"email\" ],\n" +
            "    \"optionalClientScopes\" : [ \"address\", \"phone\", \"offline_access\" ]\n" +
            "  },    {\n" +
            "      \"id\": \"f943c108-d8d6-46fe-9c44-a16965fb50ea\",\n" +
            "      \"clientId\": \"quarkus-client\",\n" +
            "      \"rootUrl\": \"http://localhost:8080\",\n" +
            "      \"adminUrl\": \"http://localhost:8080\",\n" +
            "      \"surrogateAuthRequired\": false,\n" +
            "      \"enabled\": true,\n" +
            "      \"clientAuthenticatorType\": \"client-secret\",\n" +
            "      \"secret\": \"mysecret\",\n" +
            "      \"redirectUris\": [\n" +
            "        \"http://localhost:8080/*\"\n" +
            "      ],\n" +
            "      \"webOrigins\": [\n" +
            "        \"http://localhost:8080\"\n" +
            "      ],\n" +
            "      \"notBefore\": 0,\n" +
            "      \"bearerOnly\": false,\n" +
            "      \"consentRequired\": false,\n" +
            "      \"standardFlowEnabled\": true,\n" +
            "      \"implicitFlowEnabled\": false,\n" +
            "      \"directAccessGrantsEnabled\": true,\n" +
            "      \"serviceAccountsEnabled\": true,\n" +
            "      \"authorizationServicesEnabled\": true,\n" +
            "      \"publicClient\": false,\n" +
            "      \"frontchannelLogout\": false,\n" +
            "      \"protocol\": \"openid-connect\",\n" +
            "      \"attributes\": {\n" +
            "        \"saml.assertion.signature\": \"false\",\n" +
            "        \"saml.force.post.binding\": \"false\",\n" +
            "        \"saml.multivalued.roles\": \"false\",\n" +
            "        \"saml.encrypt\": \"false\",\n" +
            "        \"saml.server.signature\": \"false\",\n" +
            "        \"saml.server.signature.keyinfo.ext\": \"false\",\n" +
            "        \"exclude.session.state.from.auth.response\": \"false\",\n" +
            "        \"saml_force_name_id_format\": \"false\",\n" +
            "        \"saml.client.signature\": \"false\",\n" +
            "        \"tls.client.certificate.bound.access.tokens\": \"false\",\n" +
            "        \"saml.authnstatement\": \"false\",\n" +
            "        \"display.on.consent.screen\": \"false\",\n" +
            "        \"saml.onetimeuse.condition\": \"false\"\n" +
            "      },\n" +
            "      \"authenticationFlowBindingOverrides\": {},\n" +
            "      \"fullScopeAllowed\": true,\n" +
            "      \"nodeReRegistrationTimeout\": -1,\n" +
            "      \"protocolMappers\": [\n" +
            "        {\n" +
            "          \"id\": \"298e0ced-d186-4eaa-92ed-7dc5e94cc0a5\",\n" +
            "          \"name\": \"Client Host\",\n" +
            "          \"protocol\": \"openid-connect\",\n" +
            "          \"protocolMapper\": \"oidc-usersessionmodel-note-mapper\",\n" +
            "          \"consentRequired\": false,\n" +
            "          \"config\": {\n" +
            "            \"user.session.note\": \"clientHost\",\n" +
            "            \"id.token.claim\": \"true\",\n" +
            "            \"access.token.claim\": \"true\",\n" +
            "            \"claim.name\": \"clientHost\",\n" +
            "            \"jsonType.label\": \"String\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"5611ae18-0db8-41cf-8d49-e360fb1b6f14\",\n" +
            "          \"name\": \"realm roles\",\n" +
            "          \"protocol\": \"openid-connect\",\n" +
            "          \"protocolMapper\": \"oidc-usermodel-realm-role-mapper\",\n" +
            "          \"consentRequired\": false,\n" +
            "          \"config\": {\n" +
            "            \"user.attribute\": \"foo\",\n" +
            "            \"access.token.claim\": \"true\",\n" +
            "            \"claim.name\": \"groups\",\n" +
            "            \"jsonType.label\": \"String\",\n" +
            "            \"multivalued\": \"true\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"247e19ae-63d3-4c79-9f1b-b58b3b6dea9f\",\n" +
            "          \"name\": \"Client IP Address\",\n" +
            "          \"protocol\": \"openid-connect\",\n" +
            "          \"protocolMapper\": \"oidc-usersessionmodel-note-mapper\",\n" +
            "          \"consentRequired\": false,\n" +
            "          \"config\": {\n" +
            "            \"user.session.note\": \"clientAddress\",\n" +
            "            \"id.token.claim\": \"true\",\n" +
            "            \"access.token.claim\": \"true\",\n" +
            "            \"claim.name\": \"clientAddress\",\n" +
            "            \"jsonType.label\": \"String\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"c7d5b9da-79d1-4ced-90d1-8e81d4b9e7ff\",\n" +
            "          \"name\": \"Client ID\",\n" +
            "          \"protocol\": \"openid-connect\",\n" +
            "          \"protocolMapper\": \"oidc-usersessionmodel-note-mapper\",\n" +
            "          \"consentRequired\": false,\n" +
            "          \"config\": {\n" +
            "            \"user.session.note\": \"clientId\",\n" +
            "            \"id.token.claim\": \"true\",\n" +
            "            \"access.token.claim\": \"true\",\n" +
            "            \"claim.name\": \"clientId\",\n" +
            "            \"jsonType.label\": \"String\"\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"defaultClientScopes\": [\n" +
            "        \"web-origins\",\n" +
            "        \"role_list\",\n" +
            "        \"roles\",\n" +
            "        \"profile\",\n" +
            "        \"email\"\n" +
            "      ],\n" +
            "      \"optionalClientScopes\": [\n" +
            "        \"address\",\n" +
            "        \"phone\",\n" +
            "        \"offline_access\"\n" +
            "      ],\n" +
            "      \"authorizationSettings\": {\n" +
            "        \"allowRemoteResourceManagement\": true,\n" +
            "        \"policyEnforcementMode\": \"ENFORCING\",\n" +
            "        \"resources\": [\n" +
            "          {\n" +
            "            \"name\": \"Default Resource\",\n" +
            "            \"type\": \"urn:quarkus-client:resources:default\",\n" +
            "            \"ownerManagedAccess\": false,\n" +
            "            \"attributes\": {},\n" +
            "            \"_id\": \"efd9ba6c-ffe1-49f3-8297-3940a8ba0e51\",\n" +
            "            \"uris\": [\n" +
            "              \"/*\"\n" +
            "            ]\n" +
            "          }\n" +
            "        ],\n" +
            "        \"policies\": [\n" +
            "          {\n" +
            "            \"id\": \"48dbcd86-997c-4cb4-8200-7c3ad0c2011f\",\n" +
            "            \"name\": \"Default Policy\",\n" +
            "            \"description\": \"A policy that grants access only for users within this realm\",\n" +
            "            \"type\": \"js\",\n" +
            "            \"logic\": \"POSITIVE\",\n" +
            "            \"decisionStrategy\": \"AFFIRMATIVE\",\n" +
            "            \"config\": {\n" +
            "              \"code\": \"// by default, grants any permission associated with this policy\\n$evaluation.grant();\\n\"\n" +
            "            }\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"253a1d21-4e33-45ad-985a-f8fa9b84ef46\",\n" +
            "            \"name\": \"Default Permission\",\n" +
            "            \"description\": \"A permission that applies to the default resource type\",\n" +
            "            \"type\": \"resource\",\n" +
            "            \"logic\": \"POSITIVE\",\n" +
            "            \"decisionStrategy\": \"UNANIMOUS\",\n" +
            "            \"config\": {\n" +
            "              \"defaultResourceType\": \"urn:quarkus-client:resources:default\",\n" +
            "              \"applyPolicies\": \"[\\\"Default Policy\\\"]\"\n" +
            "            }\n" +
            "          }\n" +
            "        ],\n" +
            "        \"scopes\": []\n" +
            "      }\n" +
            "    },\n" +
            "  {\n" +
            "    \"id\" : \"df94d122-f41f-4f2f-80fc-f1395124cc05\",\n" +
            "    \"clientId\" : \"realm-management\",\n" +
            "    \"name\" : \"${client_realm-management}\",\n" +
            "    \"surrogateAuthRequired\" : false,\n" +
            "    \"enabled\" : true,\n" +
            "    \"clientAuthenticatorType\" : \"client-secret\",\n" +
            "    \"secret\" : \"**********\",\n" +
            "    \"redirectUris\" : [ ],\n" +
            "    \"webOrigins\" : [ ],\n" +
            "    \"notBefore\" : 0,\n" +
            "    \"bearerOnly\" : true,\n" +
            "    \"consentRequired\" : false,\n" +
            "    \"standardFlowEnabled\" : true,\n" +
            "    \"implicitFlowEnabled\" : false,\n" +
            "    \"directAccessGrantsEnabled\" : false,\n" +
            "    \"serviceAccountsEnabled\" : false,\n" +
            "    \"publicClient\" : false,\n" +
            "    \"frontchannelLogout\" : false,\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : { },\n" +
            "    \"authenticationFlowBindingOverrides\" : { },\n" +
            "    \"fullScopeAllowed\" : false,\n" +
            "    \"nodeReRegistrationTimeout\" : 0,\n" +
            "    \"defaultClientScopes\" : [ \"web-origins\", \"role_list\", \"roles\", \"profile\", \"email\" ],\n" +
            "    \"optionalClientScopes\" : [ \"address\", \"phone\", \"offline_access\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"34cd583d-aba1-4421-bd75-0db99c533e0f\",\n" +
            "    \"clientId\" : \"security-admin-console\",\n" +
            "    \"name\" : \"${client_security-admin-console}\",\n" +
            "    \"baseUrl\" : \"/auth/admin/quarkus-realm/console/index.html\",\n" +
            "    \"surrogateAuthRequired\" : false,\n" +
            "    \"enabled\" : true,\n" +
            "    \"clientAuthenticatorType\" : \"client-secret\",\n" +
            "    \"secret\" : \"**********\",\n" +
            "    \"redirectUris\" : [ \"/auth/admin/quarkus-realm/console/*\" ],\n" +
            "    \"webOrigins\" : [ ],\n" +
            "    \"notBefore\" : 0,\n" +
            "    \"bearerOnly\" : false,\n" +
            "    \"consentRequired\" : false,\n" +
            "    \"standardFlowEnabled\" : true,\n" +
            "    \"implicitFlowEnabled\" : false,\n" +
            "    \"directAccessGrantsEnabled\" : false,\n" +
            "    \"serviceAccountsEnabled\" : false,\n" +
            "    \"publicClient\" : true,\n" +
            "    \"frontchannelLogout\" : false,\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : { },\n" +
            "    \"authenticationFlowBindingOverrides\" : { },\n" +
            "    \"fullScopeAllowed\" : false,\n" +
            "    \"nodeReRegistrationTimeout\" : 0,\n" +
            "    \"protocolMappers\" : [ {\n" +
            "      \"id\" : \"6e970b27-78fa-40d9-9457-b2d038f9d10e\",\n" +
            "      \"name\" : \"locale\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"locale\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"locale\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    } ],\n" +
            "    \"defaultClientScopes\" : [ \"web-origins\", \"role_list\", \"roles\", \"profile\", \"email\" ],\n" +
            "    \"optionalClientScopes\" : [ \"address\", \"phone\", \"offline_access\" ]\n" +
            "  } ],\n" +
            "  \"clientScopes\" : [ {\n" +
            "    \"id\" : \"00fa6d87-0bc7-4b40-ad84-4545e09408c6\",\n" +
            "    \"name\" : \"address\",\n" +
            "    \"description\" : \"OpenID Connect built-in scope: address\",\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : {\n" +
            "      \"include.in.token.scope\" : \"true\",\n" +
            "      \"display.on.consent.screen\" : \"true\",\n" +
            "      \"consent.screen.text\" : \"${addressScopeConsentText}\"\n" +
            "    },\n" +
            "    \"protocolMappers\" : [ {\n" +
            "      \"id\" : \"27c29249-7a2f-4c86-90cf-34d3ee83786b\",\n" +
            "      \"name\" : \"address\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-address-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"user.attribute.formatted\" : \"formatted\",\n" +
            "        \"user.attribute.country\" : \"country\",\n" +
            "        \"user.attribute.postal_code\" : \"postal_code\",\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute.street\" : \"street\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"user.attribute.region\" : \"region\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"user.attribute.locality\" : \"locality\"\n" +
            "      }\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"31a2a3d3-a750-4bf8-a5f4-e19cd4ce7ba3\",\n" +
            "    \"name\" : \"email\",\n" +
            "    \"description\" : \"OpenID Connect built-in scope: email\",\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : {\n" +
            "      \"include.in.token.scope\" : \"true\",\n" +
            "      \"display.on.consent.screen\" : \"true\",\n" +
            "      \"consent.screen.text\" : \"${emailScopeConsentText}\"\n" +
            "    },\n" +
            "    \"protocolMappers\" : [ {\n" +
            "      \"id\" : \"566d6b22-b306-4e09-99a2-8c901a9cfaeb\",\n" +
            "      \"name\" : \"email\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-property-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"email\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"email\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"994d732e-3ec8-47ec-9e87-c1a93a85dc0b\",\n" +
            "      \"name\" : \"email verified\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-property-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"emailVerified\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"email_verified\",\n" +
            "        \"jsonType.label\" : \"boolean\"\n" +
            "      }\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"c7604a83-afdd-4d10-87f1-6065a7db66e9\",\n" +
            "    \"name\" : \"offline_access\",\n" +
            "    \"description\" : \"OpenID Connect built-in scope: offline_access\",\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : {\n" +
            "      \"consent.screen.text\" : \"${offlineAccessScopeConsentText}\",\n" +
            "      \"display.on.consent.screen\" : \"true\"\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"id\" : \"94ca9143-1b05-4a75-8620-d161a301e1ab\",\n" +
            "    \"name\" : \"phone\",\n" +
            "    \"description\" : \"OpenID Connect built-in scope: phone\",\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : {\n" +
            "      \"include.in.token.scope\" : \"true\",\n" +
            "      \"display.on.consent.screen\" : \"true\",\n" +
            "      \"consent.screen.text\" : \"${phoneScopeConsentText}\"\n" +
            "    },\n" +
            "    \"protocolMappers\" : [ {\n" +
            "      \"id\" : \"28153a56-bef3-4f4a-848e-020d4e772c80\",\n" +
            "      \"name\" : \"phone number verified\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"phoneNumberVerified\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"phone_number_verified\",\n" +
            "        \"jsonType.label\" : \"boolean\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"d6984c72-fc4e-4806-9184-f24dbb1562c4\",\n" +
            "      \"name\" : \"phone number\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"phoneNumber\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"phone_number\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"c5231f53-2b58-43cd-94b0-99b362b521b4\",\n" +
            "    \"name\" : \"profile\",\n" +
            "    \"description\" : \"OpenID Connect built-in scope: profile\",\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : {\n" +
            "      \"include.in.token.scope\" : \"true\",\n" +
            "      \"display.on.consent.screen\" : \"true\",\n" +
            "      \"consent.screen.text\" : \"${profileScopeConsentText}\"\n" +
            "    },\n" +
            "    \"protocolMappers\" : [ {\n" +
            "      \"id\" : \"ead3ce30-0014-4f5a-9c5a-d2b3ff69ec69\",\n" +
            "      \"name\" : \"birthdate\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"birthdate\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"birthdate\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"27341395-8081-44db-8e80-d1f8d1b444b2\",\n" +
            "      \"name\" : \"website\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"website\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"website\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"3df539e2-dd32-4aa6-8955-bfcd3c48ba31\",\n" +
            "      \"name\" : \"family name\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-property-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"lastName\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"family_name\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"9a722698-1ea0-4e00-b909-5a451e855aaa\",\n" +
            "      \"name\" : \"username\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-property-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"username\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"preferred_username\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"c7aae599-f895-447f-be06-7b579e81a51a\",\n" +
            "      \"name\" : \"updated at\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"updatedAt\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"updated_at\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"04a4e1e5-20ca-4ad0-9449-61f353deab8d\",\n" +
            "      \"name\" : \"nickname\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"nickname\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"nickname\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"7f47144a-cb2a-4ca2-9acc-242a9fda642e\",\n" +
            "      \"name\" : \"picture\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"picture\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"picture\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"8f0ea5b5-cfd5-47a1-adac-636477854d3d\",\n" +
            "      \"name\" : \"middle name\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"middleName\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"middle_name\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"c4368f07-ce04-4082-809f-65ab432bc655\",\n" +
            "      \"name\" : \"profile\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"profile\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"profile\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"89e0406f-011d-4430-bc2e-ce12183fda3f\",\n" +
            "      \"name\" : \"given name\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-property-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"firstName\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"given_name\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"9a0f410f-d401-4287-bd3c-a19eaaafb307\",\n" +
            "      \"name\" : \"zoneinfo\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"zoneinfo\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"zoneinfo\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"7133475d-e119-4006-8c57-c5aeee02281c\",\n" +
            "      \"name\" : \"full name\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-full-name-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"userinfo.token.claim\" : \"true\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"a1598f7c-c9fd-49d9-b932-2a8f2f2208aa\",\n" +
            "      \"name\" : \"locale\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"locale\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"locale\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"c6d91274-4429-4ea6-a02a-af494d77c99c\",\n" +
            "      \"name\" : \"gender\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-attribute-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"userinfo.token.claim\" : \"true\",\n" +
            "        \"user.attribute\" : \"gender\",\n" +
            "        \"id.token.claim\" : \"true\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"gender\",\n" +
            "        \"jsonType.label\" : \"String\"\n" +
            "      }\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"1ad94696-c33f-4dd7-bc27-62b4b139391e\",\n" +
            "    \"name\" : \"role_list\",\n" +
            "    \"description\" : \"SAML role list\",\n" +
            "    \"protocol\" : \"saml\",\n" +
            "    \"attributes\" : {\n" +
            "      \"consent.screen.text\" : \"${samlRoleListScopeConsentText}\",\n" +
            "      \"display.on.consent.screen\" : \"true\"\n" +
            "    },\n" +
            "    \"protocolMappers\" : [ {\n" +
            "      \"id\" : \"42842c24-1530-4289-b6c3-66cec01ebe30\",\n" +
            "      \"name\" : \"role list\",\n" +
            "      \"protocol\" : \"saml\",\n" +
            "      \"protocolMapper\" : \"saml-role-list-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"single\" : \"false\",\n" +
            "        \"attribute.nameformat\" : \"Basic\",\n" +
            "        \"attribute.name\" : \"Role\"\n" +
            "      }\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"b5d6f8c7-ede4-497d-8fdc-26b26002b504\",\n" +
            "    \"name\" : \"roles\",\n" +
            "    \"description\" : \"OpenID Connect scope for add user roles to the access token\",\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : {\n" +
            "      \"include.in.token.scope\" : \"false\",\n" +
            "      \"display.on.consent.screen\" : \"true\",\n" +
            "      \"consent.screen.text\" : \"${rolesScopeConsentText}\"\n" +
            "    },\n" +
            "    \"protocolMappers\" : [ {\n" +
            "      \"id\" : \"a07d65ca-6861-4fd1-ba2c-ace78c0b3190\",\n" +
            "      \"name\" : \"realm roles\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-realm-role-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"user.attribute\" : \"foo\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"realm_access.roles\",\n" +
            "        \"jsonType.label\" : \"String\",\n" +
            "        \"multivalued\" : \"true\"\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"2849f8d3-c631-4852-a19e-1257ad43df09\",\n" +
            "      \"name\" : \"audience resolve\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-audience-resolve-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : { }\n" +
            "    }, {\n" +
            "      \"id\" : \"763fa554-fa7d-431e-9e9e-5470159c7350\",\n" +
            "      \"name\" : \"client roles\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-usermodel-client-role-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : {\n" +
            "        \"user.attribute\" : \"foo\",\n" +
            "        \"access.token.claim\" : \"true\",\n" +
            "        \"claim.name\" : \"resource_access.${client_id}.roles\",\n" +
            "        \"jsonType.label\" : \"String\",\n" +
            "        \"multivalued\" : \"true\"\n" +
            "      }\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"1217c5a0-b87d-4be8-a975-6128a4131bab\",\n" +
            "    \"name\" : \"web-origins\",\n" +
            "    \"description\" : \"OpenID Connect scope for add allowed web origins to the access token\",\n" +
            "    \"protocol\" : \"openid-connect\",\n" +
            "    \"attributes\" : {\n" +
            "      \"include.in.token.scope\" : \"false\",\n" +
            "      \"display.on.consent.screen\" : \"false\",\n" +
            "      \"consent.screen.text\" : \"\"\n" +
            "    },\n" +
            "    \"protocolMappers\" : [ {\n" +
            "      \"id\" : \"87a8c55f-8d87-42a7-a2da-25d7a4fcbd93\",\n" +
            "      \"name\" : \"allowed web origins\",\n" +
            "      \"protocol\" : \"openid-connect\",\n" +
            "      \"protocolMapper\" : \"oidc-allowed-origins-mapper\",\n" +
            "      \"consentRequired\" : false,\n" +
            "      \"config\" : { }\n" +
            "    } ]\n" +
            "  } ],\n" +
            "  \"defaultDefaultClientScopes\" : [ \"web-origins\", \"role_list\", \"email\", \"roles\", \"profile\" ],\n" +
            "  \"defaultOptionalClientScopes\" : [ \"address\", \"phone\", \"offline_access\" ],\n" +
            "  \"browserSecurityHeaders\" : {\n" +
            "    \"contentSecurityPolicyReportOnly\" : \"\",\n" +
            "    \"xContentTypeOptions\" : \"nosniff\",\n" +
            "    \"xRobotsTag\" : \"none\",\n" +
            "    \"xFrameOptions\" : \"SAMEORIGIN\",\n" +
            "    \"xXSSProtection\" : \"1; mode=block\",\n" +
            "    \"contentSecurityPolicy\" : \"frame-src 'self'; frame-ancestors 'self'; object-src 'none';\",\n" +
            "    \"strictTransportSecurity\" : \"max-age=31536000; includeSubDomains\"\n" +
            "  },\n" +
            "  \"smtpServer\" : { },\n" +
            "  \"eventsEnabled\" : false,\n" +
            "  \"eventsListeners\" : [ \"jboss-logging\" ],\n" +
            "  \"enabledEventTypes\" : [ ],\n" +
            "  \"adminEventsEnabled\" : false,\n" +
            "  \"adminEventsDetailsEnabled\" : false,\n" +
            "  \"components\" : {\n" +
            "    \"org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy\" : [ {\n" +
            "      \"id\" : \"2a33b301-fad6-482e-ba5e-e1593242a986\",\n" +
            "      \"name\" : \"Consent Required\",\n" +
            "      \"providerId\" : \"consent-required\",\n" +
            "      \"subType\" : \"anonymous\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : { }\n" +
            "    }, {\n" +
            "      \"id\" : \"b57223db-9905-408d-8fa3-60ea5a1190e1\",\n" +
            "      \"name\" : \"Allowed Protocol Mapper Types\",\n" +
            "      \"providerId\" : \"allowed-protocol-mappers\",\n" +
            "      \"subType\" : \"anonymous\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : {\n" +
            "        \"allowed-protocol-mapper-types\" : [ \"oidc-sha256-pairwise-sub-mapper\", \"saml-user-attribute-mapper\", \"saml-user-property-mapper\", \"oidc-usermodel-property-mapper\", \"oidc-address-mapper\", \"saml-role-list-mapper\", \"oidc-full-name-mapper\", \"oidc-usermodel-attribute-mapper\" ]\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"cabd60da-1b1c-4247-b351-2aa2641628db\",\n" +
            "      \"name\" : \"Allowed Protocol Mapper Types\",\n" +
            "      \"providerId\" : \"allowed-protocol-mappers\",\n" +
            "      \"subType\" : \"authenticated\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : {\n" +
            "        \"allowed-protocol-mapper-types\" : [ \"oidc-usermodel-attribute-mapper\", \"oidc-sha256-pairwise-sub-mapper\", \"saml-user-attribute-mapper\", \"oidc-address-mapper\", \"oidc-usermodel-property-mapper\", \"saml-role-list-mapper\", \"saml-user-property-mapper\", \"oidc-full-name-mapper\" ]\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"2c0469ef-4e25-4176-8fd1-2bf90cd2e055\",\n" +
            "      \"name\" : \"Trusted Hosts\",\n" +
            "      \"providerId\" : \"trusted-hosts\",\n" +
            "      \"subType\" : \"anonymous\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : {\n" +
            "        \"host-sending-registration-request-must-match\" : [ \"true\" ],\n" +
            "        \"client-uris-must-match\" : [ \"true\" ]\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"2571af38-43f4-4835-8d6e-c5b8d1f20ff3\",\n" +
            "      \"name\" : \"Allowed Client Scopes\",\n" +
            "      \"providerId\" : \"allowed-client-templates\",\n" +
            "      \"subType\" : \"anonymous\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : {\n" +
            "        \"allow-default-scopes\" : [ \"true\" ]\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"663eaea9-ee71-4cb1-8afb-731d3cd130ca\",\n" +
            "      \"name\" : \"Full Scope Disabled\",\n" +
            "      \"providerId\" : \"scope\",\n" +
            "      \"subType\" : \"anonymous\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : { }\n" +
            "    }, {\n" +
            "      \"id\" : \"2e7e2102-0fe5-458e-8622-df68140fe13e\",\n" +
            "      \"name\" : \"Max Clients Limit\",\n" +
            "      \"providerId\" : \"max-clients\",\n" +
            "      \"subType\" : \"anonymous\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : {\n" +
            "        \"max-clients\" : [ \"200\" ]\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"4b52e27f-9112-45ed-b6a4-b4c2ea5e38b0\",\n" +
            "      \"name\" : \"Allowed Client Scopes\",\n" +
            "      \"providerId\" : \"allowed-client-templates\",\n" +
            "      \"subType\" : \"authenticated\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : {\n" +
            "        \"allow-default-scopes\" : [ \"true\" ]\n" +
            "      }\n" +
            "    } ],\n" +
            "    \"org.keycloak.keys.KeyProvider\" : [ {\n" +
            "      \"id\" : \"d89ada22-9397-47e0-8722-fd24185b334c\",\n" +
            "      \"name\" : \"aes-generated\",\n" +
            "      \"providerId\" : \"aes-generated\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : {\n" +
            "        \"kid\" : [ \"7c4bf2ad-032b-4090-995a-dfa112d78ca5\" ],\n" +
            "        \"secret\" : [ \"3k-M2ie5NYVCG3HQM9XvKQ\" ],\n" +
            "        \"priority\" : [ \"100\" ]\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"0a7be844-8a9d-453f-9af0-3a8decde0af1\",\n" +
            "      \"name\" : \"rsa-generated\",\n" +
            "      \"providerId\" : \"rsa-generated\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : {\n" +
            "        \"privateKey\" : [ \"MIIEowIBAAKCAQEAq1M+7pongdkiSk3Uu4b3ulQmYixvvvS5bPRbxFuTaLRLfNKTwsX0iFOwClvBihkmt/XrGmovQAQGPxMedvexG8EbC0dGKoha/Bm2ojSyz/BBaUSop5mRSGUGA5tu30WNGhz5FD5DSjNhuUnIrqMfEQY64Zn2vByFwJ3PdOKucfqZImsp99IBAfovcnXmRKTX+cs0U2dDukKwm4ffTxu6U7AX8a7ZSB4ZuySqdkxVW9gXMWx8CBPwms2JQv0Y0zv8pnoSyiGHlj9QkmO5kvCWuGZpNtW0OLOXGBBa7FBDKVFI1EzLiXU82c9VpVUc2ZeqqHeh5FhZ7m8Rdx+ErWr3EQIDAQABAoIBAAfGSU7OlAhicBnrGkJAR7Ge6/b8iT2jIV7+X8OzXCceN2a8r5PhI82j7pMpwsifh5BFipuyQd6n0BjAp1tCtjjKNI34eAiqE2bpS85kkdC52F4MjZK9d0hPvUwgm5FqOJL8yZjPkoIlfsPHw2GZaWl+XOGr+PRkXvV0s9tcBgqJ/4WXfaSpSNX2LrHKN3VlZJ9wIiqvtby04fq/RIghQW9DoBMoFLdZ4liQ1ETQaOofiAsi57dNxPzlgOL6CqY0adQD0bjpwDVb6u7FQzvgO+cq09hYvosRwRpa3NvPpDCjG80TgGzrMpO6Ra/CfV+c5UXsUm8ARxSO4Ywl0tN3bjkCgYEA8BkcRvHlK2nuWhdTHZSVIgYjo11DtK5TTxwra3oL7yme0GJhT2Gvf2xxrXcspXdtQdIz95V9+1wl5Tua8cBs28aKrf4c/5yl9CMGExokwFu0QwcDHSUgrGXb9mOLqLb5sDN1pp8zygPZ0tSBlWbDuSQ3vSy9l+uRE9r/aZmZtS8CgYEAtqwVR4z9Z6Y/bK+WXfuQ1AINAHMjAX8OT3PkLPRpsqxM50F5RybEiTmYO1lGylvbaTLk8nBkWeZ3oItZZ5yd42CRau5+qtPirPIwi1hN1hG+dRtI9W97JOBYnR3Qe28hrD0MYlbrlbRQoiVXThxhjcQnwrAj9hp0yugZ/xOfh78CgYBAT1mFnzE1CbCwLOM0XyH69Q4Cqe6CrNbfr7nplc0wiENjqZZK/u1VAiUIw40P3QdExBrrS/1soGf+GJMOAe6mh22J4TC/PqQs2ly8LRp+85E7CtvIqLMxui7KtisoS6phiccPlED48UMc95KmA9qU8qCDaUJ3OL+pQ9V54YyMPQKBgQCuE3zPy36saFPmick8VH/mWPH07MN+KIsFkgXuBRg4qVM7yTv+XuZJcrYsUF5Sdoi3TkW49vvaY/K57jFLuIcJaDwLb60Ls5E+iClUkNjT1+LsjAzRvuPV92jaiM/k4Llw/lgv2m70IXn5mqYBDLC2PSg6skoCu6Fyvw4O8q9C1wKBgEmjo5USk1FzaK9t85CFPpoWnA1RQfH9qLoHL0FMNkQ61U2wdtOOjzRHoJWN5ijcSHkI5EXY/KNkYOFU5YJv25lCple+N1yCnn95LW37uG1qVfbnRDJVZ/wXNygxjwOpclB9vW4dY0k7jy6wlqgo32KUfKstJsxD4AQ8L5gOqu2u\" ],\n" +
            "        \"certificate\" : [ \"MIICszCCAZsCBgFppeOniDANBgkqhkiG9w0BAQsFADAdMRswGQYDVQQDDBJxdWFya3VzLXF1aWNrc3RhcnQwHhcNMTkwMzIyMTQ1MDU0WhcNMjkwMzIyMTQ1MjM0WjAdMRswGQYDVQQDDBJxdWFya3VzLXF1aWNrc3RhcnQwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCrUz7umieB2SJKTdS7hve6VCZiLG++9Lls9FvEW5NotEt80pPCxfSIU7AKW8GKGSa39esaai9ABAY/Ex5297EbwRsLR0YqiFr8GbaiNLLP8EFpRKinmZFIZQYDm27fRY0aHPkUPkNKM2G5Sciuox8RBjrhmfa8HIXAnc904q5x+pkiayn30gEB+i9ydeZEpNf5yzRTZ0O6QrCbh99PG7pTsBfxrtlIHhm7JKp2TFVb2BcxbHwIE/CazYlC/RjTO/ymehLKIYeWP1CSY7mS8Ja4Zmk21bQ4s5cYEFrsUEMpUUjUTMuJdTzZz1WlVRzZl6qod6HkWFnubxF3H4StavcRAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAG4fmpvk4QhmEu6r+uyKfDmeREvlVqyXJrKc+Z6LFlQ3vf+pRgQbb98zrDTN/m2GRYVOW3MT+aoqWaVo7BqLmsosY8Nm82FD/gjAS6cswpri26GM9TbY+sQF6IL4nGeo5/1pyU8sh2FMY3YACktL6HilMG+1RMWIGdlDGcGxSPYh5fNftL4x8z52GhwfW6Xctu+N82DJFJt5TA6JROM8pW8D3efoq60n6AZkimrzAHTc86MLZXnroyGcplxLnTXS84u6QZ9lU110H3a2F7gxbZVpTjMNKnD++E58Wr1kpViDY7o2il8XNnSUzh1U+P60LNNzEOP2GD1e4Dt8AhiwuS8=\" ],\n" +
            "        \"priority\" : [ \"100\" ]\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"id\" : \"2e64aa1a-79b5-4339-b1da-a0af40d6c6c8\",\n" +
            "      \"name\" : \"hmac-generated\",\n" +
            "      \"providerId\" : \"hmac-generated\",\n" +
            "      \"subComponents\" : { },\n" +
            "      \"config\" : {\n" +
            "        \"kid\" : [ \"5f04bf08-2833-4e7a-9850-2fef5bcbf7c6\" ],\n" +
            "        \"secret\" : [ \"2nZE4SIzEYV5UFdRwVf_Wy1SgNU0M6UOtY7u-arHBGtwBq3uzxZg7BR1KXWaMpIpzLuhlqWpH968JRAoWRJBAQ\" ],\n" +
            "        \"priority\" : [ \"100\" ],\n" +
            "        \"algorithm\" : [ \"HS256\" ]\n" +
            "      }\n" +
            "    } ]\n" +
            "  },\n" +
            "  \"internationalizationEnabled\" : false,\n" +
            "  \"supportedLocales\" : [ ],\n" +
            "  \"authenticationFlows\" : [ {\n" +
            "    \"id\" : \"a5126b37-8ad3-45ba-9dcb-05055493b311\",\n" +
            "    \"alias\" : \"Handle Existing Account\",\n" +
            "    \"description\" : \"Handle what to do if there is existing account with same email/username like authenticated identity provider\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : false,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"idp-confirm-link\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"idp-email-verification\",\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 20,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 30,\n" +
            "      \"flowAlias\" : \"Verify Existing Account by Re-authentication\",\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : true\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"0c258a6b-45b1-4625-b156-276e2737a61e\",\n" +
            "    \"alias\" : \"Verify Existing Account by Re-authentication\",\n" +
            "    \"description\" : \"Reauthentication of existing account\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : false,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"idp-username-password-form\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"auth-otp-form\",\n" +
            "      \"requirement\" : \"OPTIONAL\",\n" +
            "      \"priority\" : 20,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"407620b7-e810-444c-80a9-a77706cd7b90\",\n" +
            "    \"alias\" : \"browser\",\n" +
            "    \"description\" : \"browser based authentication\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : true,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"auth-cookie\",\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"auth-spnego\",\n" +
            "      \"requirement\" : \"DISABLED\",\n" +
            "      \"priority\" : 20,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"identity-provider-redirector\",\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 25,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 30,\n" +
            "      \"flowAlias\" : \"forms\",\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : true\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"788ae0e7-30f6-4bc5-9c58-d3bc226eb478\",\n" +
            "    \"alias\" : \"clients\",\n" +
            "    \"description\" : \"Base authentication for clients\",\n" +
            "    \"providerId\" : \"client-flow\",\n" +
            "    \"topLevel\" : true,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"client-secret\",\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"client-jwt\",\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 20,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"client-secret-jwt\",\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 30,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"client-x509\",\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 40,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"725230ad-7d5e-4c83-9bd1-a6df2c89ee0e\",\n" +
            "    \"alias\" : \"direct grant\",\n" +
            "    \"description\" : \"OpenID Connect Resource Owner Grant\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : true,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"direct-grant-validate-username\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"direct-grant-validate-password\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 20,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"direct-grant-validate-otp\",\n" +
            "      \"requirement\" : \"OPTIONAL\",\n" +
            "      \"priority\" : 30,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"f8fe0824-46bb-4f84-a0e7-403364411973\",\n" +
            "    \"alias\" : \"docker auth\",\n" +
            "    \"description\" : \"Used by Docker clients to authenticate against the IDP\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : true,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"docker-http-basic-authenticator\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"ac209e90-885b-474d-a220-e6cc6f2e563f\",\n" +
            "    \"alias\" : \"first broker login\",\n" +
            "    \"description\" : \"Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : true,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticatorConfig\" : \"review profile config\",\n" +
            "      \"authenticator\" : \"idp-review-profile\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticatorConfig\" : \"create unique user config\",\n" +
            "      \"authenticator\" : \"idp-create-user-if-unique\",\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 20,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"requirement\" : \"ALTERNATIVE\",\n" +
            "      \"priority\" : 30,\n" +
            "      \"flowAlias\" : \"Handle Existing Account\",\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : true\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"e344b270-e0be-4b90-8883-41057fadfd3f\",\n" +
            "    \"alias\" : \"forms\",\n" +
            "    \"description\" : \"Username, password, otp and other auth forms.\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : false,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"auth-username-password-form\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"auth-otp-form\",\n" +
            "      \"requirement\" : \"OPTIONAL\",\n" +
            "      \"priority\" : 20,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"4cfb1eb0-d6b1-4599-8690-12df2bc32740\",\n" +
            "    \"alias\" : \"http challenge\",\n" +
            "    \"description\" : \"An authentication flow based on challenge-response HTTP Authentication Schemes\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : true,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"no-cookie-redirect\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"basic-auth\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 20,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"basic-auth-otp\",\n" +
            "      \"requirement\" : \"DISABLED\",\n" +
            "      \"priority\" : 30,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"auth-spnego\",\n" +
            "      \"requirement\" : \"DISABLED\",\n" +
            "      \"priority\" : 40,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"75b649e7-59ce-4184-8b37-d0b80e94d33f\",\n" +
            "    \"alias\" : \"registration\",\n" +
            "    \"description\" : \"registration flow\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : true,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"registration-page-form\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"flowAlias\" : \"registration form\",\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : true\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"17b2e1f2-dec9-4593-963d-15e79ab13dda\",\n" +
            "    \"alias\" : \"registration form\",\n" +
            "    \"description\" : \"registration form\",\n" +
            "    \"providerId\" : \"form-flow\",\n" +
            "    \"topLevel\" : false,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"registration-user-creation\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 20,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"registration-profile-action\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 40,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"registration-password-action\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 50,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"registration-recaptcha-action\",\n" +
            "      \"requirement\" : \"DISABLED\",\n" +
            "      \"priority\" : 60,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"e61ac977-d70f-4abc-b8bd-dbf410b08b57\",\n" +
            "    \"alias\" : \"reset credentials\",\n" +
            "    \"description\" : \"Reset credentials for a user if they forgot their password or something\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : true,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"reset-credentials-choose-user\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"reset-credential-email\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 20,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"reset-password\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 30,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    }, {\n" +
            "      \"authenticator\" : \"reset-otp\",\n" +
            "      \"requirement\" : \"OPTIONAL\",\n" +
            "      \"priority\" : 40,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"id\" : \"99e6d97a-1193-43ba-acbc-2f7617c2d9a0\",\n" +
            "    \"alias\" : \"saml ecp\",\n" +
            "    \"description\" : \"SAML ECP Profile Authentication Flow\",\n" +
            "    \"providerId\" : \"basic-flow\",\n" +
            "    \"topLevel\" : true,\n" +
            "    \"builtIn\" : true,\n" +
            "    \"authenticationExecutions\" : [ {\n" +
            "      \"authenticator\" : \"http-basic-authenticator\",\n" +
            "      \"requirement\" : \"REQUIRED\",\n" +
            "      \"priority\" : 10,\n" +
            "      \"userSetupAllowed\" : false,\n" +
            "      \"autheticatorFlow\" : false\n" +
            "    } ]\n" +
            "  } ],\n" +
            "  \"authenticatorConfig\" : [ {\n" +
            "    \"id\" : \"4a9d2a9d-f116-4ba4-88f3-be823951ca94\",\n" +
            "    \"alias\" : \"create unique user config\",\n" +
            "    \"config\" : {\n" +
            "      \"require.password.update.after.registration\" : \"false\"\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"id\" : \"c9a9453c-e445-4a01-a525-86100e6e404c\",\n" +
            "    \"alias\" : \"review profile config\",\n" +
            "    \"config\" : {\n" +
            "      \"update.profile.on.first.login\" : \"missing\"\n" +
            "    }\n" +
            "  } ],\n" +
            "  \"requiredActions\" : [ {\n" +
            "    \"alias\" : \"CONFIGURE_TOTP\",\n" +
            "    \"name\" : \"Configure OTP\",\n" +
            "    \"providerId\" : \"CONFIGURE_TOTP\",\n" +
            "    \"enabled\" : true,\n" +
            "    \"defaultAction\" : false,\n" +
            "    \"priority\" : 10,\n" +
            "    \"config\" : { }\n" +
            "  }, {\n" +
            "    \"alias\" : \"terms_and_conditions\",\n" +
            "    \"name\" : \"Terms and Conditions\",\n" +
            "    \"providerId\" : \"terms_and_conditions\",\n" +
            "    \"enabled\" : false,\n" +
            "    \"defaultAction\" : false,\n" +
            "    \"priority\" : 20,\n" +
            "    \"config\" : { }\n" +
            "  }, {\n" +
            "    \"alias\" : \"UPDATE_PASSWORD\",\n" +
            "    \"name\" : \"Update Password\",\n" +
            "    \"providerId\" : \"UPDATE_PASSWORD\",\n" +
            "    \"enabled\" : true,\n" +
            "    \"defaultAction\" : false,\n" +
            "    \"priority\" : 30,\n" +
            "    \"config\" : { }\n" +
            "  }, {\n" +
            "    \"alias\" : \"UPDATE_PROFILE\",\n" +
            "    \"name\" : \"Update Profile\",\n" +
            "    \"providerId\" : \"UPDATE_PROFILE\",\n" +
            "    \"enabled\" : true,\n" +
            "    \"defaultAction\" : false,\n" +
            "    \"priority\" : 40,\n" +
            "    \"config\" : { }\n" +
            "  }, {\n" +
            "    \"alias\" : \"VERIFY_EMAIL\",\n" +
            "    \"name\" : \"Verify Email\",\n" +
            "    \"providerId\" : \"VERIFY_EMAIL\",\n" +
            "    \"enabled\" : true,\n" +
            "    \"defaultAction\" : false,\n" +
            "    \"priority\" : 50,\n" +
            "    \"config\" : { }\n" +
            "  } ],\n" +
            "  \"browserFlow\" : \"browser\",\n" +
            "  \"registrationFlow\" : \"registration\",\n" +
            "  \"directGrantFlow\" : \"direct grant\",\n" +
            "  \"resetCredentialsFlow\" : \"reset credentials\",\n" +
            "  \"clientAuthenticationFlow\" : \"clients\",\n" +
            "  \"dockerAuthenticationFlow\" : \"docker auth\",\n" +
            "  \"attributes\" : {\n" +
            "    \"_browser_header.xXSSProtection\" : \"1; mode=block\",\n" +
            "    \"_browser_header.xFrameOptions\" : \"SAMEORIGIN\",\n" +
            "    \"_browser_header.strictTransportSecurity\" : \"max-age=31536000; includeSubDomains\",\n" +
            "    \"permanentLockout\" : \"false\",\n" +
            "    \"quickLoginCheckMilliSeconds\" : \"1000\",\n" +
            "    \"_browser_header.xRobotsTag\" : \"none\",\n" +
            "    \"maxFailureWaitSeconds\" : \"900\",\n" +
            "    \"minimumQuickLoginWaitSeconds\" : \"60\",\n" +
            "    \"failureFactor\" : \"30\",\n" +
            "    \"actionTokenGeneratedByUserLifespan\" : \"300\",\n" +
            "    \"maxDeltaTimeSeconds\" : \"43200\",\n" +
            "    \"_browser_header.xContentTypeOptions\" : \"nosniff\",\n" +
            "    \"offlineSessionMaxLifespan\" : \"5184000\",\n" +
            "    \"actionTokenGeneratedByAdminLifespan\" : \"43200\",\n" +
            "    \"_browser_header.contentSecurityPolicyReportOnly\" : \"\",\n" +
            "    \"bruteForceProtected\" : \"false\",\n" +
            "    \"_browser_header.contentSecurityPolicy\" : \"frame-src 'self'; frame-ancestors 'self'; object-src 'none';\",\n" +
            "    \"waitIncrementSeconds\" : \"60\",\n" +
            "    \"offlineSessionMaxLifespanEnabled\" : \"false\"\n" +
            "  },\n" +
            "  \"keycloakVersion\" : \"5.0.0\",\n" +
            "  \"userManagedAccessAllowed\" : false\n" +
            "}";

    public JSONWriter() {
    }

    public JSONWriter(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getConfig(){
        return config;
    }

    public void crearConfigJson(String nombre) {
        String path = System.getProperty("user.dir");
        String userHome = System.getProperty("user.home");

        try {
            File myObj = new File(path + "/" + nombre + "/quarkus-realm.json");
            if (myObj.createNewFile()) {
                // System.out.println("File created: " + myObj.getName());
            } else {
                //  System.out.println("Archivo ya existe.");
            }
        } catch (
                IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/quarkus-realm.json");

            myWriter.write(config);
            myWriter.close();
            //   System.out.println("Clase api generado");
        } catch (
                IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }


    }


}
