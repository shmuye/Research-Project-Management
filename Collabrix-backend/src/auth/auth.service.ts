import { ForbiddenException, Injectable } from '@nestjs/common';
import { PrismaService } from 'src/prisma/prisma.service';
import { signinDto, signupDto } from './dto';
import * as bcrypt from 'bcrypt';
import { Tokens } from './types';
import { Role } from '@prisma/client';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class AuthService {
    constructor(
        private prisma: PrismaService,
        private jwtService: JwtService) { }


    async signup(dto: signupDto): Promise<Tokens> {
        const hash = await this.hashData(dto.password)
        const newUser = await this.prisma.user.create({
            data: {
                name: dto.name,
                email: dto.email,
                hash,
                role: dto.role
            }
        })
        const tokens = await this.getTokens(newUser.id, newUser.email, newUser.role)
        await this.updateRtHash(newUser.id, tokens.refresh_token)
        return tokens
    }

    async signin(dto: signinDto): Promise<Tokens> {
        const user = await this.prisma.user.findUnique({
            where: {
                email: dto.email
            }
        })
        if (!user) throw new ForbiddenException("Access Denied")
        const passwordMatched = await bcrypt.compare(dto.password, user.hash)
        if (!passwordMatched) throw new ForbiddenException("Access Denied")

        const tokens = await this.getTokens(user.id, user.email, user.role)
        await this.updateRtHash(user.id, tokens.refresh_token)
        return tokens
    }
    async logout(userId: number) {
        await this.prisma.user.updateMany({
            where: {
                id: userId,
                hashedRt: {
                    not: null
                }
            },
            data: {
                hashedRt: null
            }
        })
    }
    async refreshTokens(userId: number, rt: string) {
        const user = await this.prisma.user.findUnique({
            where: {
                id: userId
            }
        })
        if (!user) throw new ForbiddenException("Access Denied")
        const rtMatched = await bcrypt.compare(rt, user.hashedRt)
        if (!rtMatched) throw new ForbiddenException("Access Denied")

        const tokens = await this.getTokens(user.id, user.email, user.role)
        await this.updateRtHash(user.id, tokens.refresh_token)
        return tokens
    }

    hashData(data: string) {
        return bcrypt.hash(data, 10);
    }

    async getTokens(userId: number, email: string, role: Role) {
        const [at, rt] = await Promise.all([
            this.jwtService.signAsync({
                sub: userId,
                email,
                role,
            }, {
                secret: 'at-secret',
                expiresIn: 60 * 15,

            }),
            this.jwtService.signAsync({
                sub: userId,
                email,
                role,
            }, {
                secret: 'rt-secret',
                expiresIn: 60 * 60 * 24 * 7,

            }),
        ]);
        return {
            access_token: at,
            refresh_token: rt
        }

    }
    async updateRtHash(userId: number, rt: string) {
        const hash = await this.hashData(rt);
        await this.prisma.user.update(
            {
                where: {
                    id: userId
                }, data: {
                    hashedRt: hash
                }
            }
        )
    }
}
