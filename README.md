# Project Summary

An onboarding portal for the Users(Dispatcher/Driver/Customer) of Verda-Flow platform to bring them onboard much prior to the launch of actual platform.



## Development Environments

**Development**

This is the working environment for individual developers or small teams.
Working in isolation with the rest of the tiers, the developer(s) can try radical changes to the code without adversely affecting the rest of the development team.

**QA**

This is exactly like "Development" but only meant for quality assurance team.

**Staging**

The staging tier is a environment that is as identical to the production environment as possible.
The purpose of the Staging environment is to simulate as much of the Production environment as possible.
The Staging environment can also double as a Demonstration/Training environment.

**Production**

The production tier consists of 2 servers with a load balancer.

## Deployment Environment

![Alt text](https://bitbucket.org/org/verdaflow-java/downloads/Disp-Deployement-Layer.png)

## API Reference

```
Dev: https://dev.verdaflow.com/verdaflow/swagger-ui.html

QA: https://qa.verdaflow.com/verdaflow/swagger-ui.html

Staging: https://staging.verdaflow.com/verdaflow/swagger-ui.html
```

## APIs Testing Level

```
Level 1: Postman Testing (By Developer)

Level 2: JMeter Testing (By Dedicated Performance Testing Team)
```

## Security Testing Level

```
End to End Security Testing (By Dedicated Security Testing Team)
```

