import { Module } from '@nestjs/common';
import { AuthModule } from './auth/auth.module';
import { UserModule } from './user/user.module';
import { ApplicationModule } from './application/application.module';
import { ProfileModule } from './profile/profile.module';
import { ProjectModule } from './project/project.module';
import { PrismaModule } from './prisma/prisma.module';
import { APP_GUARD } from '@nestjs/core';
import { AtGuard } from './common/guards/at.guard';
import { RolesGuard } from './common/guards/roles.guard';
@Module({
  imports: [AuthModule, UserModule, ApplicationModule, ProfileModule, ProjectModule, PrismaModule],
  providers: [{
    provide: APP_GUARD,
    useClass: AtGuard
  }, {
    provide: APP_GUARD,
    useClass: RolesGuard
  }
  ]
})
export class AppModule { }
