import { Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from 'src/prisma/prisma.service';
import { UpdateProfileDto } from 'src/profile/dto/update-profile.dto';

@Injectable()
export class UserService {
    constructor(private readonly prisma: PrismaService) { }

    async getProfile(userId: number) {
        return this.prisma.user.findUnique({
            where: { id: userId },
            select: {
                id: true,
                email: true,
                name: true,
                bio: true,
                role: true,
                skills: true,
                researchInterests: true,
            },
        });
    }

    async updateProfile(userId: number, dto: UpdateProfileDto) {
        const user = await this.prisma.user.findUnique({ where: { id: userId } });
        if (!user) throw new NotFoundException('User not found');

        const updateData: any = {
            name: dto.name,
            bio: dto.bio,
        };

        if (user.role === 'STUDENT') {
            updateData.skills = dto.skills;
        } else if (user.role === 'PROFESSOR') {
            updateData.researchInterests = dto.researchInterests;
        }

        return this.prisma.user.update({
            where: { id: userId },
            data: updateData,
        });
    }

    async deleteAccount(userId: number) {
        return this.prisma.user.delete({
            where: { id: userId },
        });
    }

    async getAllUsers() {
        return this.prisma.user.findMany({
            select: {
                id: true,
                email: true,
                name: true,
                role: true,
            },
        });
    }

    async getUserById(id: number) {
        return this.prisma.user.findUnique({
            where: { id },
            select: {
                id: true,
                email: true,
                name: true,
                role: true,
                bio: true,
                skills: true,
                researchInterests: true,
            },
        });
    }
}
