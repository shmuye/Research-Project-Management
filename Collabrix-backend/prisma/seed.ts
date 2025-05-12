import { PrismaClient, Role } from '@prisma/client';
import * as bcrypt from 'bcrypt';
import * as dotenv from 'dotenv';

dotenv.config(); // Load .env values

const prisma = new PrismaClient();

async function main() {
    const adminEmail = process.env.ADMIN_EMAIL;
    const adminPassword = process.env.ADMIN_PASSWORD;

    if (!adminEmail || !adminPassword) {
        throw new Error('ADMIN_EMAIL or ADMIN_PASSWORD not set in .env');
    }

    const hashedPassword = await bcrypt.hash(adminPassword, 10);

    const admin = await prisma.user.upsert({
        where: { email: adminEmail },
        update: {}, // No update for existing admin
        create: {
            email: adminEmail,
            hash: hashedPassword,
            role: Role.ADMIN,
        },
    });

    console.log('Admin user seeded:', admin.email);
}

main()
    .catch((e) => {
        console.error(e);
        process.exit(1);
    })
    .finally(() => prisma.$disconnect());
