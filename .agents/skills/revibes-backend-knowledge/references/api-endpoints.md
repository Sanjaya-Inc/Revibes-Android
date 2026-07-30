# Revibes Backend API Endpoints Reference

All API routes are served under the Cloud Function `v1` prefix: `https://<region>-<project_id>.cloudfunctions.net/v1/<group>/<path>`

---

## 1. Authentication (`/auth`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `POST` | `/auth/register` | No | Register new user with email & password | `RegisterDto` (`email`, `password`, `name`, `phone?`) | `AuthResponseDto` (`token`, `refreshToken`, `user`) |
| `POST` | `/auth/login` | No | Login with email & password | `LoginDto` (`email`, `password`) | `AuthResponseDto` (`token`, `refreshToken`, `user`) |
| `POST` | `/auth/login/phone` | No | Login / verify via phone number | `PhoneLoginDto` (`phoneNumber`, `otpCode?`) | `AuthResponseDto` (`token`, `refreshToken`, `user`) |
| `POST` | `/auth/register/phone` | No | Register user with phone number | `PhoneRegisterDto` (`phoneNumber`, `name`) | `AuthResponseDto` (`token`, `refreshToken`, `user`) |
| `POST` | `/auth/refresh` | No | Refresh JWT access token | `RefreshTokenDto` (`refreshToken`) | `TokenResponseDto` (`token`, `refreshToken`) |
| `POST` | `/auth/logout` | Yes | Invalidate user session / refresh token | - | `MessageResponseDto` |
| `POST` | `/auth/forgot-password` | No | Request password reset email | `ForgotPasswordDto` (`email`) | `MessageResponseDto` |
| `POST` | `/auth/reset-password` | No | Reset password with token | `ResetPasswordDto` (`token`, `newPassword`) | `MessageResponseDto` |

---

## 2. Me / User Profile (`/me` & `/users`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `GET` | `/me` | Yes | Get authenticated user profile | - | `UserDetailDto` |
| `PUT` | `/me` | Yes | Update user profile details | `UpdateProfileDto` (`name?`, `avatarUrl?`, `address?`, `geoPoint?`) | `UserDetailDto` |
| `POST` | `/me/avatar` | Yes | Upload profile picture | Form-Data file (`file`) | `UserDetailDto` |
| `POST` | `/me/device` | Yes | Register FCM token & device info | `RegisterDeviceDto` (`fcmToken`, `deviceModel`, `osVersion`) | `UserDeviceDto` |
| `GET` | `/me/point-histories` | Yes | Get point history transactions | Pagination Query (`page?`, `limit?`, `type?`) | `PaginatedResponseDto<UserPointHistoryDto>` |
| `GET` | `/users` | Yes (Admin) | List all users with pagination & search | Query (`page?`, `limit?`, `search?`, `role?`) | `PaginatedResponseDto<UserPublicDto>` |
| `GET` | `/users/:id` | Yes (Admin) | Get user details by ID | - | `UserDetailDto` |
| `PATCH` | `/users/:id/status` | Yes (Admin) | Update user account status | `UpdateUserStatusDto` (`status`: `ACTIVE` \| `SUSPENDED`) | `UserDetailDto` |
| `PATCH` | `/users/:id/role` | Yes (Admin) | Update user role | `UpdateUserRoleDto` (`role`: `USER` \| `ADMIN`) | `UserDetailDto` |

---

## 3. Banners (`/banners`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `GET` | `/banners` | No | Get active promotional & hero banners | Query (`type?`, `isActive?`) | `ListResponseDto<BannerDto>` |
| `GET` | `/banners/:id` | No | Get banner detail by ID | - | `BannerDto` |
| `POST` | `/banners` | Yes (Admin) | Create banner | `CreateBannerDto` (`title`, `imageUrl`, `targetUrl`, `order`, `isActive`) | `BannerDto` |
| `PUT` | `/banners/:id` | Yes (Admin) | Update banner | `UpdateBannerDto` | `BannerDto` |
| `DELETE` | `/banners/:id` | Yes (Admin) | Delete banner | - | `MessageResponseDto` |

---

## 4. Countries (`/countries`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `GET` | `/countries` | No | List supported countries | Query (`isActive?`) | `ListResponseDto<CountryDto>` |
| `GET` | `/countries/:id` | No | Get country detail | - | `CountryDto` |
| `POST` | `/countries` | Yes (Admin) | Add new country | `CreateCountryDto` (`code`, `name`, `phoneCode`, `currency`, `flagUrl`) | `CountryDto` |

---

## 5. Points Exchange & Rewards (`/exchange`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `GET` | `/exchange/items` | No | List redeemable point items | Query (`category?`, `page?`, `limit?`) | `PaginatedResponseDto<ExchangeItemDto>` |
| `GET` | `/exchange/items/:id` | No | Get exchange item details | - | `ExchangeItemDto` |
| `POST` | `/exchange/transactions` | Yes | Redeem points for items | `CreateExchangeTransactionDto` (`itemId`, `quantity`) | `ExchangeTransactionDto` |
| `GET` | `/exchange/transactions` | Yes | Get user's exchange transaction history | Query (`page?`, `limit?`, `status?`) | `PaginatedResponseDto<ExchangeTransactionDto>` |
| `GET` | `/exchange/transactions/:id` | Yes | Get exchange transaction detail | - | `ExchangeTransactionDto` |

---

## 6. Waste Inventory & Drop-off Categories (`/inventory`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `GET` | `/inventory/items` | No | List recyclable inventory categories & points/kg | Query (`category?`, `isActive?`) | `ListResponseDto<InventoryItemDto>` |
| `GET` | `/inventory/items/:id` | No | Get inventory item details | - | `InventoryItemDto` |

---

## 7. Logistic Orders (Drop-off & Pick-up) (`/logistic-orders`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `POST` | `/logistic-orders` | Yes | Create waste drop-off / pick-up order | `CreateLogisticOrderDto` (`type`: `DROP_OFF` \| `PICK_UP`, `storeBranchId?`, `pickupAddress?`, `items`: `[{inventoryItemId, weightKg}]`) | `LogisticOrderDto` |
| `GET` | `/logistic-orders` | Yes | List user's logistic orders | Query (`page?`, `limit?`, `status?`, `type?`) | `PaginatedResponseDto<LogisticOrderDto>` |
| `GET` | `/logistic-orders/:id` | Yes | Get order details & items | - | `LogisticOrderDto` |
| `POST` | `/logistic-orders/:id/cancel` | Yes | Cancel order | `CancelOrderDto` (`reason?`) | `LogisticOrderDto` |
| `PATCH` | `/logistic-orders/:id/status` | Yes (Admin/Courier) | Update order status | `UpdateOrderStatusDto` (`status`: `SUBMITTED` \| `PROCESSING` \| `COMPLETED` \| `CANCELLED`, `verifiedItems?`) | `LogisticOrderDto` |

---

## 8. Missions & Daily Rewards (`/missions` & `/me`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `GET` | `/missions` | No | List active global missions | Query (`isActive?`) | `ListResponseDto<MissionDto>` |
| `GET` | `/me/missions` | Yes | Get user's assigned missions & progress | Query (`status?`: `IN_PROGRESS` \| `COMPLETED` \| `CLAIMED`) | `ListResponseDto<UserMissionDto>` |
| `POST` | `/me/missions/:id/claim` | Yes | Claim mission completion points | - | `ClaimMissionResultDto` (`pointsEarned`, `userMission`) |
| `GET` | `/me/daily-rewards` | Yes | Get user's daily check-in streak status | - | `DailyRewardStatusDto` (`currentStreak`, `canClaimToday`, `rewards`) |
| `POST` | `/me/daily-rewards/claim` | Yes | Claim daily check-in reward | - | `ClaimDailyRewardResultDto` (`pointsEarned`, `streak`) |

---

## 9. Store Branches & Locations (`/stores`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `GET` | `/stores` | No | List all store drop-off locations | Query (`city?`, `isActive?`) | `ListResponseDto<StoreBranchDto>` |
| `GET` | `/stores/nearest` | No | Find nearest store locations by coordinates | Query (`lat`, `lng`, `radiusKm?`) | `ListResponseDto<StoreBranchDto>` |
| `GET` | `/stores/:id` | No | Get store branch detail | - | `StoreBranchDto` |

---

## 10. Vouchers & Promotions (`/vouchers` & `/me/vouchers`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `GET` | `/vouchers` | No | List available public vouchers | Query (`page?`, `limit?`, `category?`) | `PaginatedResponseDto<VoucherDto>` |
| `GET` | `/vouchers/:id` | No | Get voucher detail | - | `VoucherDto` |
| `GET` | `/me/vouchers` | Yes | List user's claimed vouchers | Query (`status?`: `AVAILABLE` \| `USED` \| `EXPIRED`) | `ListResponseDto<UserVoucherDto>` |
| `POST` | `/me/vouchers/claim` | Yes | Claim/redeem voucher | `ClaimVoucherDto` (`voucherId`) | `UserVoucherDto` |
| `POST` | `/me/vouchers/:id/use` | Yes | Use claimed voucher at checkout/partner | `UseVoucherDto` (`orderId?`) | `UserVoucherDto` |

---

## 11. System Settings (`/settings`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `GET` | `/settings` | No | Get public app configuration settings | - | `Map<string, any>` |
| `GET` | `/settings/:key` | No | Get setting value by key | - | `AppSettingDto` |

---

## 12. Daily Check-In News (`/news`)

| Method | Path | Auth Required | Description | Request Payload DTO | Response DTO |
|--------|------|---------------|-------------|---------------------|--------------|
| `GET` | `/news` | Yes | Get active check-in daily news | - | `NewsResponse` (`id`, `title`, `content`, `createdAt`, `isActive`) |
| `POST` | `/news` | Yes (Admin) | Create active check-in daily news | `CreateNewsDto` (`title`, `content`) | `NewsResponse` |
| `PUT` | `/news/:id` | Yes (Admin) | Update active daily news | `UpdateNewsDto` (`title?`, `content?`) | `NewsResponse` |
| `DELETE` | `/news/:id` | Yes (Admin) | Delete daily news by ID | - | `NewsResponse` (`data: null`) |

