import { IsOptional, IsString, IsArray, IsEnum, IsNotEmpty } from 'class-validator';
import { Role } from '@prisma/client';

export class UpdateProfileDto {

    @IsString()
    name: string;

    @IsString()
    department: string;

    @IsOptional()
    @IsString()
    bio?: string;

    @IsOptional()
    @IsArray()
    @IsString({ each: true })
    skills?: string[]; // For STUDENT

    @IsOptional()
    @IsString()
    researchInterests?: string; // For PROFESSOR

    @IsOptional()
    @IsEnum(Role)
    role?: Role; // Optional if needed for switching roles
}
