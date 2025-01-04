import { isPlatformBrowser } from '@angular/common';
import { PLATFORM_ID, inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { lastValueFrom } from 'rxjs';

// prevent user from accessing other sites than the login page
export const authGuard: CanActivateFn = (): boolean => {
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  // not a pretty way of checking if user is logged in but works as of now
    const isLoggedIn = localStorage.getItem('isLoggedIn');
    if (!isLoggedIn) {
      router.navigate(['/']);
      return false;
    }
  return true;
};