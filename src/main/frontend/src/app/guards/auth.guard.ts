import { isPlatformBrowser } from '@angular/common';
import { PLATFORM_ID, inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { lastValueFrom } from 'rxjs';
import { AuthService } from '../services/auth.service';

// prevent user from accessing other sites than the login page
export const authGuard: CanActivateFn = (): boolean => {
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);
  const authService = inject(AuthService)

  // not a pretty way of checking if user is logged in but works as of now
    if (!authService.getLoggedInStatus()) {
      router.navigate(['/']);
      return false;
    }
  return true;
};