import { Component, inject } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { filter, map, startWith } from 'rxjs';

type PublicPageRoute = {
  path: string;
  label: string;
};

const PUBLIC_PAGE_ROUTES: PublicPageRoute[] = [
  { path: '/', label: 'Home' },
  { path: '/about-us', label: 'About Us' },
  { path: '/academics', label: 'Academics' },
  { path: '/facilities', label: 'Facilities' },
  { path: '/contact', label: 'Contact' },
  { path: '/request-admission', label: 'Request Admission' },
];

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  private readonly router = inject(Router);
  readonly publicPageRoutes = PUBLIC_PAGE_ROUTES;
  currentRoute = '/';
  mobileNavOpen = false;

  constructor() {
    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd),
        startWith(null),
        map(() => this.normalizeRoute(this.router.url)),
      )
      .subscribe((route) => {
        this.currentRoute = route;
        this.mobileNavOpen = false;
      });
  }

  toggleMobileNav(): void {
    this.mobileNavOpen = !this.mobileNavOpen;
  }

  closeMobileNav(): void {
    this.mobileNavOpen = false;
  }

  get currentPageIndex(): number {
    return this.publicPageRoutes.findIndex((page) => page.path === this.currentRoute);
  }

  get hasPager(): boolean {
    return !this.isAdminRoute && this.currentPageIndex >= 0;
  }

  get isAdminRoute(): boolean {
    return this.currentRoute === '/admin' || this.currentRoute.startsWith('/admin/');
  }

  get previousPage(): PublicPageRoute | null {
    const index = this.currentPageIndex;
    return index > 0 ? this.publicPageRoutes[index - 1] : null;
  }

  get nextPage(): PublicPageRoute | null {
    const index = this.currentPageIndex;
    return index >= 0 && index < this.publicPageRoutes.length - 1 ? this.publicPageRoutes[index + 1] : null;
  }

  private normalizeRoute(url: string): string {
    const cleanedUrl = url.split('?')[0].split('#')[0];
    return cleanedUrl === '' ? '/' : cleanedUrl;
  }
}
