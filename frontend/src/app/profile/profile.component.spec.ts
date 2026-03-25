import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TokenStorageService } from '../_services/token-storage.service';
import { ProfileComponent } from './profile.component';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let tokenStorageSpy: jasmine.SpyObj<TokenStorageService>;

  beforeEach(async () => {
    tokenStorageSpy = jasmine.createSpyObj<TokenStorageService>(
      'TokenStorageService',
      ['getUser'],
    );

    await TestBed.configureTestingModule({
      declarations: [ProfileComponent],
      providers: [{ provide: TokenStorageService, useValue: tokenStorageSpy }],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit should load currentUser from token storage', () => {
    const fakeUser = { id: 1, username: 'hamza' };
    tokenStorageSpy.getUser.and.returnValue(fakeUser);

    component.ngOnInit();

    expect(tokenStorageSpy.getUser).toHaveBeenCalled();
    expect(component.currentUser).toEqual(fakeUser);
  });
});
