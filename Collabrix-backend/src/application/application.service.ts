import { Injectable, NotFoundException, BadRequestException, ForbiddenException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';
import { ApplicationStatus } from '@prisma/client';

@Injectable()
export class ApplicationService {
    constructor(private readonly prisma: PrismaService) { }

    async apply(projectId: number, studentId: number) {
        const project = await this.prisma.project.findUnique({ where: { id: projectId } });
        if (!project) throw new NotFoundException('Project not found');

        const existing = await this.prisma.application.findFirst({
            where: { projectId, studentId },
        });
        if (existing) throw new BadRequestException('Already applied to this project');

        return this.prisma.application.create({
            data: {
                projectId,
                studentId,
                status: ApplicationStatus.PENDING,
            },
        });
    }

    async getStudentApplications(studentId: number) {
        return this.prisma.application.findMany({
            where: { studentId },
            include: { project: true },
        });
    }

    async getProjectApplications(projectId: number, professorId: number) {
        const project = await this.prisma.project.findUnique({
            where: { id: projectId },
        });

        if (!project) {
            throw new NotFoundException('Project not found');
        }

        if (project.professorId !== professorId) {
            throw new ForbiddenException('Forbidden: You can only view applications for your own projects');
        }

        return this.prisma.application.findMany({
            where: { projectId },
            include: { student: true },
        });
    }


    async updateStatus(applicationId: number, status: ApplicationStatus, professorId: number) {
        const application = await this.prisma.application.findUnique({
            where: { id: applicationId },
            include: {
                project: true, // Include project to access professorId
            },
        });

        if (!application) {
            throw new NotFoundException('Application not found');
        }

        if (application.project.professorId !== professorId) {
            throw new ForbiddenException('Forbidden: You can only update applications for your own projects');
        }

        return this.prisma.application.update({
            where: { id: applicationId },
            data: { status },
        });
    }

    // application.service.ts

    async getParticipants(projectId: number) {
        return this.prisma.application.findMany({
            where: {
                projectId,
                status: 'APPROVED',
            },
            include: {
                student: true, // Assuming relation exists
            },
        });
    }

    async removeParticipant(projectId: number, studentId: number) {
        const application = await this.prisma.application.findFirst({
            where: {
                projectId,
                studentId,
            },
        });

        if (!application) {
            throw new NotFoundException('Participant not found in this project.');
        }

        return this.prisma.application.deleteMany({
            where: {
                projectId,
                studentId,
            },
        });
    }



}

