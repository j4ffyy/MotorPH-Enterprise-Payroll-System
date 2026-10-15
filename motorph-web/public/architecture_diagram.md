# MotorPH EPS — Security Architecture Diagram (Mermaid Source)
## IAS2 Controls R1–R8 | Group 8 CTRL+ALT+ELITE

```mermaid
graph TB
    subgraph CLIENT["🌐 Browser / Client Layer"]
        BROWSER["Next.js 16 React App\nMotorPH EPS UI"]
    end

    subgraph EDGE["☁️ Vercel Edge Network"]
        HEADERS["🛡️ R8: Security Headers\nX-Frame-Options: DENY\nX-Content-Type-Options: nosniff\nReferrer-Policy\nPermissions-Policy\nHSTS max-age=63072000"]
    end

    subgraph MIDDLEWARE["🔐 Next.js Middleware (src/middleware.ts)"]
        NONCE["🛡️ R8: CSP Nonce Generator\ncrypto.randomBytes(16).toString('base64')\nPer-request 128-bit nonce injected into\nContent-Security-Policy header"]
        CSRF["🛡️ R7: CSRF Double-Submit Cookie\ngenerateCSRFToken() → motorph_csrf cookie\nvalidateCsrfToken() → x-csrf-token header\ncrypto.timingSafeEqual() comparison"]
    end

    subgraph RATELIMIT["⏱️ Rate Limiting Layer (src/lib/rateLimit.ts)"]
        RL["🛡️ R6: Login Rate Limiter\n5 failed attempts / 15-min window / IP\nHTTP 429 + Retry-After header\nIn-memory Map keyed by IP"]
    end

    subgraph VALIDATION["✅ Input Validation Layer (src/lib/validators.ts)"]
        ZOD["🛡️ R2: Zod Schema Validation\nemployeeSchema | payrollSchema\nleaveSchema | loginSchema\nHTTP 400 on violation"]
    end

    subgraph RBAC_LAYER["🔒 Access Control Layer (src/lib/rbac.ts)"]
        RBAC["🛡️ R3: RBAC Role Guard\nROLE_PERMISSIONS map\nADMIN | HR | PAYROLL | EMPLOYEE\nrequireRole() → HTTP 403"]
        IDOR["🛡️ R4: IDOR Self-Check\nrequireSelfOrElevated()\nJWT userId === requested EID\nHTTP 403 on cross-user access"]
        PII["🛡️ R5: PII Response Filter\nfilterEmployeeResponse()\npasswordHash always stripped\ngovIds redacted for EMPLOYEE role"]
    end

    subgraph ROUTES["🔌 Next.js API Routes"]
        LOGIN["/api/auth/login\nBCrypt verify (10 rounds)\nJWT sign → HttpOnly cookie"]
        EMP["/api/employees\n/api/employees/[eid]"]
        PAY["/api/payroll\nStatutory computation\nSSS/PhilHealth/Pag-IBIG/BIR"]
        LEAVES["/api/leaves"]
        AUDIT["/api/audit-logs\nADMIN only"]
    end

    subgraph ORM["🗄️ Data Layer"]
        PRISMA["Prisma ORM 6.19.3\nParameterized queries\nType-safe schema"]
    end

    subgraph DB["🐘 Neon PostgreSQL 15"]
        TABLES["Employee | Payroll\nLeave | Timesheet"]
        AUDITLOG["🛡️ R1: AuditLog Table\nuserId | action | entity\noldValues | newValues\nipAddress | userAgent | createdAt\nNon-repudiation record"]
    end

    %% Edges
    BROWSER -->|"HTTPS request"| EDGE
    EDGE -->|"x-nonce header injected"| MIDDLEWARE
    MIDDLEWARE --> RATELIMIT
    RATELIMIT -->|"Pass (≤5 attempts)"| VALIDATION
    RATELIMIT -->|"HTTP 429"| BROWSER
    VALIDATION -->|"Pass (valid schema)"| RBAC_LAYER
    VALIDATION -->|"HTTP 400"| BROWSER
    RBAC_LAYER -->|"Authorized"| ROUTES
    RBAC_LAYER -->|"HTTP 403"| BROWSER
    ROUTES --> ORM
    ORM --> DB
    ROUTES -->|"logAuditEvent()"| AUDITLOG

    %% Styling
    classDef security fill:#dc2626,stroke:#991b1b,color:#fff,font-weight:bold
    classDef db fill:#1d4ed8,stroke:#1e40af,color:#fff
    classDef middleware fill:#0f172a,stroke:#3b82f6,color:#fff
    classDef edge fill:#1f2937,stroke:#6b7280,color:#fff
    classDef route fill:#1e293b,stroke:#475569,color:#e2e8f0

    class NONCE,CSRF,RL,ZOD,RBAC,IDOR,PII,AUDITLOG security
    class DB,TABLES db
    class MIDDLEWARE,RATELIMIT,VALIDATION,RBAC_LAYER middleware
    class EDGE edge
    class ROUTES route
```

## Security Pillar Mapping

| Control | Security Pillar | Implementation |
|---------|----------------|----------------|
| **R1 AuditLog** | Accountability + Non-Repudiation | Immutable DB records with who/what/when/IP |
| **R2 Zod** | Integrity | Schema validation blocks malformed/malicious input |
| **R3 RBAC** | Confidentiality | Role-scoped data access via JWT claims |
| **R4 IDOR** | Confidentiality | Self-check prevents horizontal privilege escalation |
| **R5 PII Filter** | Confidentiality | passwordHash stripped; govIds redacted by role |
| **R6 Rate Limit** | Authenticity | Brute-force prevention on identity verification |
| **R7 CSRF** | Integrity | Double-submit cookie stops forged state mutations |
| **R8 Headers/CSP** | Integrity + Confidentiality | Nonce CSP, HSTS, X-Frame-Options, CORS lockdown |
