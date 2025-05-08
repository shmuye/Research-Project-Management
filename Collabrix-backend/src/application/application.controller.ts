import { Controller, Post, Body, Get, Param, Patch, Delete, ParseIntPipe } from '@nestjs/common';
import { ApplicationService } from './application.service';
import { Roles } from 'src/common/decorators/roles.decorator';
import { Role, ApplicationStatus } from '@prisma/client';
import { CreateApplicationDto } from './dto';
import { getCurrentUserId } from 'src/common/decorators';

@Controller('applications')
export class ApplicationController {
    constructor(private readonly applicationService: ApplicationService) { }

    @Post()
    @Roles(Role.STUDENT)
    apply(@Body() dto: CreateApplicationDto, @getCurrentUserId() userId: number) {
        return this.applicationService.apply(dto.projectId, userId);
    }

    @Get()
    @Roles(Role.STUDENT)
    getStudentApplications(@getCurrentUserId() studentId: number) {
        return this.applicationService.getStudentApplications(studentId);
    }

    @Get('project/:projectId')
    @Roles(Role.PROFESSOR)
    getProjectApplications(
        @Param('projectId') projectId: string,
        @getCurrentUserId() professorId: number
    ) {
        return this.applicationService.getProjectApplications(+projectId, professorId);
    }

    @Patch(':id')
    @Roles(Role.PROFESSOR)
    updateStatus(
        @Param('id') id: string,
        @Body('status') status: ApplicationStatus,
        @getCurrentUserId() professorId: number
    ) {
        return this.applicationService.updateStatus(+id, status, professorId);
    }


    @Get('participants/:projectId')
    @Roles(Role.PROFESSOR)
    getParticipants(@Param('projectId', ParseIntPipe) projectId: number) {
        return this.applicationService.getParticipants(projectId);
    }

    @Delete('participants/:projectId/:studentId')
    @Roles(Role.PROFESSOR)
    removeParticipant(
        @Param('projectId', ParseIntPipe) projectId: number,
        @Param('studentId', ParseIntPipe) studentId: number,
    ) {
        return this.applicationService.removeParticipant(projectId, studentId);
    }

}