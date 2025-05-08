import { IsEmail, IsNotEmpty, IsString } from "class-validator";

export class signinDto {
    @IsString()
    name: string;

    @IsEmail()
    @IsNotEmpty()
    email: string;

    @IsNotEmpty()
    password: string;

}
