import { Controller, Get, Patch, Delete, Param, Request, Body } from '@nestjs/common';
import { UserService } from './user.service';
import { Roles } from 'src/common/decorators/roles.decorator';
import { Role } from '@prisma/client';
import { UpdateProfileDto } from 'src/profile/dto/update-profile.dto';
import { getCurrentUserId } from 'src/common/decorators';

@Controller('users')
export class UserController {
    constructor(private readonly userService: UserService) { }

    @Get('me')
    getProfile(@getCurrentUserId() userId: number) {
        return this.userService.getProfile(userId);
    }

    @Patch('me')
    updateProfile(@getCurrentUserId() userId: number, @Body() dto: UpdateProfileDto) {
        return this.userService.updateProfile(userId, dto);
    }

    @Delete('me')
    deleteAccount(@getCurrentUserId() userId: number) {
        return this.userService.deleteAccount(userId);
    }

    @Roles(Role.ADMIN)
    @Get()
    getAllUsers() {
        return this.userService.getAllUsers();
    }

    @Roles(Role.ADMIN)
    @Get(':id')
    getUserById(@Param('id') id: string) {
        return this.userService.getUserById(+id);
    }

    @Roles(Role.ADMIN)
    @Delete(':id')
    deleteUserById(@Param('id') id: string) {
        return this.userService.deleteAccount(+id);
    }
}
