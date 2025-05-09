import { IsEmail, IsNotEmpty, IsOptional, IsString } from "class-validator";

export class signinDto {
    @IsOptional()
    @IsString()
    name: string;

    @IsEmail()
    @IsNotEmpty()
    email: string;

    @IsNotEmpty()
    password: string;

}
