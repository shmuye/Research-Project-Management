import { IsEmail, IsEnum, IsNotEmpty, IsString } from "class-validator";
import { Role } from "@prisma/client";
export class signupDto {

    @IsString()
    name: string;

    @IsEmail()
    @IsNotEmpty()
    email: string;

    @IsNotEmpty()
    password: string;

    @IsEnum(Role)
    role: Role
}
