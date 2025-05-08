import { Body, Controller, HttpCode, HttpStatus, Post, Req, UseGuards } from '@nestjs/common';
import { AuthService } from './auth.service';
import { signupDto, signinDto } from './dto';
import { Tokens } from './types';
import { getCurrentUser, getCurrentUserId, Public } from 'src/common/decorators';
import { RtGuard } from 'src/common/guards/rt.guard';


@Controller('auth')
export class AuthController {
    constructor(private authService: AuthService) { }

    @Public()
    @Post('signup')
    @HttpCode(HttpStatus.CREATED)
    signup(@Body() dto: signupDto): Promise<Tokens> {
        return this.authService.signup(dto)
    }

    @Public()
    @Post('signin')
    @HttpCode(HttpStatus.OK)
    signin(@Body() dto: signinDto): Promise<Tokens> {
        return this.authService.signin(dto)
    }


    @Post('logout')
    @HttpCode(HttpStatus.OK)
    logout(@getCurrentUserId() userId: number) {
        return this.authService.logout(userId)
    }

    @Public()
    @UseGuards(RtGuard)
    @Post('refresh')
    @HttpCode(HttpStatus.OK)
    refreshTokens(@getCurrentUserId() userId: number,
        @getCurrentUser('refreshToken') refreshToken: string) {
        return this.authService.refreshTokens(userId, refreshToken)
    }
}

