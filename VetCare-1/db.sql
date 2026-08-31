CREATE TABLE "role"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "name" VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE "users"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "identification_type" VARCHAR(20) CHECK ("identification_type" IN('CC', 'CE', 'PASAPORTE')) NOT NULL,
    "identification_number" VARCHAR(25) UNIQUE NOT NULL,
    "first_name" VARCHAR(255) NOT NULL,
    "last_name" VARCHAR(255) NOT NULL,
    "phone_number" VARCHAR(20) NOT NULL,
    "email" VARCHAR(255) UNIQUE NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "status" BOOLEAN NOT NULL DEFAULT true,
    "registration_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "id_rol" INT NOT NULL,
    CONSTRAINT fk_users_role FOREIGN KEY ("id_rol") REFERENCES "role"("id") ON DELETE RESTRICT
);

CREATE TABLE "owner"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "identification_type" VARCHAR(20) CHECK ("identification_type" IN('CC', 'CE', 'PASAPORTE')) NOT NULL,
    "identification_number" VARCHAR(25) UNIQUE NOT NULL,
    "first_name" VARCHAR(255) NOT NULL,
    "last_name" VARCHAR(255) NOT NULL,
    "phone_number" VARCHAR(20) NOT NULL,
    "email" VARCHAR(255) UNIQUE NOT NULL,
    "address" VARCHAR(255) NOT NULL,
    "status" BOOLEAN NOT NULL DEFAULT true,
    "registration_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "pet"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "species" VARCHAR(255) NOT NULL,
    "breed" VARCHAR(255) NOT NULL,
    "sex" VARCHAR(10) CHECK ("sex" IN('MACHO', 'HEMBRA')) NOT NULL,
    "date_of_birth" DATE NOT NULL,
    "weight" DECIMAL(5, 2) NOT NULL, 
    "status" BOOLEAN NOT NULL DEFAULT true,
    "registration_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "id_owner" INT NOT NULL,
    CONSTRAINT fk_pet_owner FOREIGN KEY ("id_owner") REFERENCES "owner"("id") ON DELETE CASCADE
);

CREATE TABLE "specialty"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "name" VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE "veterinarian"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "professional_license" VARCHAR(160) UNIQUE NOT NULL,
    "id_user" INT UNIQUE NOT NULL,
    "id_specialty" INTEGER NOT NULL,
    CONSTRAINT fk_vet_user FOREIGN KEY ("id_user") REFERENCES "users"("id") ON DELETE CASCADE,
    CONSTRAINT fk_vet_specialty FOREIGN KEY ("id_specialty") REFERENCES "specialty"("id") ON DELETE RESTRICT
);

CREATE TABLE "appointment"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "date" DATE NOT NULL,
    "hour" TIME(0) WITHOUT TIME ZONE NOT NULL,
    "reason" TEXT NOT NULL,
    "status" VARCHAR(50) CHECK ("status" IN('PROGRAMADA', 'CONFIRMADA', 'EN_ATENCION', 'FINALIZADA', 'CANCELADA')) NOT NULL DEFAULT 'PROGRAMADA',
    "registration_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "id_pet" INT NOT NULL,
    "id_veterinarian" INT NOT NULL,
    CONSTRAINT fk_app_pet FOREIGN KEY ("id_pet") REFERENCES "pet"("id") ON DELETE CASCADE,
    CONSTRAINT fk_app_vet FOREIGN KEY ("id_veterinarian") REFERENCES "veterinarian"("id") ON DELETE RESTRICT
);

CREATE TABLE "medication"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "code" VARCHAR(100) UNIQUE NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "presentation" VARCHAR(255) NOT NULL,
    "laboratory" VARCHAR(255) NOT NULL,
    "available_quantity" INT NOT NULL CHECK ("available_quantity" >= 0),
    "minimum_quantity" INT NOT NULL CHECK ("minimum_quantity" >= 0),
    "price" DECIMAL(10, 2) NOT NULL,
    "status" BOOLEAN NOT NULL DEFAULT true,
    "registration_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "attention"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "symptoms" TEXT NOT NULL,
    "diagnosis" TEXT,
    "treatment" TEXT,
    "observation" TEXT,
    "date_attention" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "status" VARCHAR(50) CHECK ("status" IN('INICIADA', 'EN_CURSO', 'FINALIZADA')) NOT NULL DEFAULT 'INICIADA',
    "id_veterinarian" INT NOT NULL,
    "id_appointment" INT UNIQUE NOT NULL,
    "id_pet" INT NOT NULL,
    CONSTRAINT fk_att_vet FOREIGN KEY ("id_veterinarian") REFERENCES "veterinarian"("id") ON DELETE RESTRICT,
    CONSTRAINT fk_att_app FOREIGN KEY ("id_appointment") REFERENCES "appointment"("id") ON DELETE RESTRICT,
    CONSTRAINT fk_att_pet FOREIGN KEY ("id_pet") REFERENCES "pet"("id") ON DELETE CASCADE
);

CREATE TABLE "attention_details"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "quantity" INT NOT NULL CHECK ("quantity" > 0),
    "id_attention" INT NOT NULL,
    "id_medication" INT NOT NULL,
    CONSTRAINT fk_det_attention FOREIGN KEY ("id_attention") REFERENCES "attention"("id") ON DELETE CASCADE,
    CONSTRAINT fk_det_medication FOREIGN KEY ("id_medication") REFERENCES "medication"("id") ON DELETE RESTRICT
);