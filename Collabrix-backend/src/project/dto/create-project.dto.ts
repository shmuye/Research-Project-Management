import { IsString, IsNotEmpty, IsArray, IsDateString } from 'class-validator';

export class CreateProjectDto {
    @IsString()
    @IsNotEmpty()
    title: string;

    @IsString()
    @IsNotEmpty()
    description: string;

    @IsArray()
    @IsString({ each: true })
    @IsNotEmpty({ each: true })
    requirements: string[];

    @IsDateString()
    startDate: string;

    @IsDateString()
    endDate: string;

    @IsDateString()
    deadline: string;
}
