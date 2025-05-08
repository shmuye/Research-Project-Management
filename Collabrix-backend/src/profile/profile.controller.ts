import { Controller, Get, Patch, Body } from '@nestjs/common';
import { ProfileService } from './profile.service';
import { Roles } from 'src/common/decorators/roles.decorator';
import { Role } from '@prisma/client';
import { UpdateProfileDto } from './dto/update-profile.dto';
import { getCurrentUserId } from 'src/common/decorators';

@Controller('profile')
export class ProfileController {
    constructor(private readonly profileService: ProfileService) { }

    @Get()
    @Roles(Role.STUDENT, Role.PROFESSOR)
    getProfile(@getCurrentUserId() userId: number) {
        return this.profileService.getProfile(userId);
    }

    @Patch()
    @Roles(Role.STUDENT, Role.PROFESSOR)
    updateProfile(@getCurrentUserId() userId: number, @Body() dto: UpdateProfileDto) {
        return this.profileService.updateProfile(userId, dto);
    }
}
