import { Injectable, NotFoundException, ForbiddenException } from '@nestjs/common';
import { PrismaService } from 'src/prisma/prisma.service';
import { CreateProjectDto } from './dto';
import { UpdateProjectDto } from './dto';

@Injectable()
export class ProjectService {
    constructor(private readonly prisma: PrismaService) { }

    async create(createProjectDto: CreateProjectDto, professorId: number) {
        return this.prisma.project.create({
            data: {
                ...createProjectDto,
                professorId,
            },
        });
    }

    async findAll() {
        return this.prisma.project.findMany();
    }

    async findOne(id: number) {
        return this.prisma.project.findUnique({
            where: { id },
        });
    }

    async findMyProjects(professorId: number) {
        return this.prisma.project.findMany({
            where: {
                professorId,
            },
        });
    }


    async update(id: number, updateProjectDto: UpdateProjectDto, professorId: number) {

        const project = await this.prisma.project.findUnique({ where: { id } });
        if (project.professorId !== professorId) {
            throw new Error('Forbidden: You can only edit your own projects.');
        }
        return this.prisma.project.update({
            where: { id },
            data: updateProjectDto,
        });
    }

    async remove(id: number) {
        return this.prisma.project.delete({
            where: { id },
        });
    }

    async deleteOwnProject(projectId: number, professorId: number) {
        const project = await this.prisma.project.findUnique({
            where: { id: projectId },
        });

        if (!project) {
            throw new NotFoundException('Project not found');
        }

        if (project.professorId !== professorId) {
            throw new ForbiddenException('You can only delete your own projects');
        }

        return this.prisma.project.delete({
            where: { id: projectId },
        });
    }
}
