# Revibes Firestore Data Models & Schema Reference

Database: Cloud Firestore

---

## 1. Collections Overview

| Collection Name | Model Class | Primary Key | Description |
|-----------------|-------------|-------------|-------------|
| `users` | `User` | `uid` | User account profiles, points balance, roles, and credentials. |
| `user_devices` | `UserDevice` | auto-id | Device FCM tokens for push notifications. |
| `banners` | `Banner` | auto-id | Dynamic hero and promo banners shown in mobile app home. |
| `countries` | `Country` | auto-id / `code` | Supported countries, phone codes, and local currencies. |
| `exchange_items` | `ExchangeItem` | auto-id | Items/rewards redeemable with user reward points. |
| `exchange_transactions` | `ExchangeTransaction` | auto-id | History of point redemption transactions. |
| `exchange_transaction_items` | `ExchangeTransactionItem` | auto-id | Item breakdown for point redemptions. |
| `inventory_items` | `InventoryItem` | auto-id | Recyclable waste categories, point values per kg, and descriptions. |
| `logistic_orders` | `LogisticOrder` | auto-id | Drop-off and pick-up waste disposal orders. |
| `logistic_order_histories` | `LogisticOrderHistory` | auto-id | Audit log of status transitions for logistic orders. |
| `missions` | `Mission` | auto-id | Gamified recycling tasks and challenges. |
| `user_missions` | `UserMission` | auto-id | User progress and completion status for assigned missions. |
| `mission_assignments` | `MissionAssignment` | auto-id | Mapping of auto-assigned missions to user tiers. |
| `app_settings` | `AppSetting` | key | Global system config key-value parameters. |
| `store_branches` | `StoreBranch` | auto-id | Waste drop-off locations and operating hours. |
| `vouchers` | `Voucher` | auto-id | Discount and promotional vouchers available for claim. |
| `user_vouchers` | `UserVoucher` | auto-id | Vouchers claimed by users and their usage state. |
| `user_point_histories` | `UserPointHistory` | auto-id | Ledger of point earnings and expenditures. |
| `user_daily_rewards` | `UserDailyReward` | auto-id | Daily check-in streak tracking for users. |
| `news` | `News` | auto-id | Daily check-in news headlines & content displayed during user check-in. |


---

## 2. Model Schemas & Fields

### User (`users`)
```typescript
interface User {
  id: string; // uid
  name: string;
  email: string;
  phoneNumber?: string;
  avatarUrl?: string;
  role: "USER" | "ADMIN" | "SUPER_ADMIN";
  status: "ACTIVE" | "SUSPENDED" | "INACTIVE";
  totalPoints: number;
  countryCode?: string;
  address?: {
    street: string;
    city: string;
    province: string;
    postalCode: string;
    geoPoint?: { latitude: number; longitude: number };
  };
  createdAt: Timestamp;
  updatedAt: Timestamp;
}
```

### LogisticOrder (`logistic_orders`)
```typescript
interface LogisticOrder {
  id: string;
  orderNumber: string;
  userId: string;
  type: "DROP_OFF" | "PICK_UP";
  status: "SUBMITTED" | "PROCESSING" | "COMPLETED" | "CANCELLED";
  storeBranchId?: string;
  pickupAddress?: {
    fullAddress: string;
    latitude: number;
    longitude: number;
    notes?: string;
  };
  totalWeightKg: number;
  totalPointsEarned: number;
  items: Array<{
    inventoryItemId: string;
    inventoryItemName: string;
    weightKg: number;
    pointsPerKg: number;
    totalPoints: number;
  }>;
  cancelReason?: string;
  completedAt?: Timestamp;
  createdAt: Timestamp;
  updatedAt: Timestamp;
}
```

### Mission & UserMission (`missions` & `user_missions`)
```typescript
interface Mission {
  id: string;
  title: string;
  description: string;
  rewardPoints: number;
  type: "DROP_OFF_WEIGHT" | "TRANSACTION_COUNT" | "DAILY_LOGIN" | "RECYCLING_STREAK";
  targetValue: number;
  imageUrl?: string;
  startDate: Timestamp;
  endDate: Timestamp;
  isActive: boolean;
}

interface UserMission {
  id: string;
  userId: string;
  missionId: string;
  currentProgress: number;
  targetValue: number;
  status: "IN_PROGRESS" | "COMPLETED" | "CLAIMED";
  completedAt?: Timestamp;
  claimedAt?: Timestamp;
}
```

### Voucher & UserVoucher (`vouchers` & `user_vouchers`)
```typescript
interface Voucher {
  id: string;
  code: string;
  title: string;
  description: string;
  discountType: "FIXED_AMOUNT" | "PERCENTAGE" | "FREE_SHIPPING";
  discountValue: number;
  minPointsToClaim: number;
  minPurchaseAmount?: number;
  maxDiscountAmount?: number;
  validUntil: Timestamp;
  totalQuantity: number;
  claimedQuantity: number;
  isActive: boolean;
}

interface UserVoucher {
  id: string;
  userId: string;
  voucherId: string;
  voucherCode: string;
  status: "AVAILABLE" | "USED" | "EXPIRED";
  claimedAt: Timestamp;
  usedAt?: Timestamp;
  expiresAt: Timestamp;
}
```

### ExchangeItem & Transaction (`exchange_items` & `exchange_transactions`)
```typescript
interface ExchangeItem {
  id: string;
  name: string;
  description: string;
  category: "ECO_PRODUCT" | "DIGITAL_COUPON" | "PLANT_TREE" | "DONATION";
  pointsRequired: number;
  stockQuantity: number;
  imageUrl: string;
  isActive: boolean;
}

interface ExchangeTransaction {
  id: string;
  userId: string;
  itemId: string;
  quantity: number;
  totalPoints: number;
  status: "PENDING" | "SUCCESS" | "FAILED";
  redemptionCode?: string;
  createdAt: Timestamp;
}
```

### StoreBranch (`store_branches`)
```typescript
interface StoreBranch {
  id: string;
  name: string;
  address: string;
  city: string;
  province: string;
  phone: string;
  latitude: number;
  longitude: number;
  operatingHours: string; // e.g. "08:00 - 20:00"
  isActive: boolean;
}
```

### News (`news`)
```typescript
interface News {
  id: string;
  title: string;
  content: string;
  createdAt: Timestamp;
  isActive: boolean;
}
```


---

## 3. Core Enums & Data Constants

- **Role**: `"USER"` | `"ADMIN"` | `"SUPER_ADMIN"`
- **Account Status**: `"ACTIVE"` | `"SUSPENDED"` | `"INACTIVE"`
- **Logistic Order Type**: `"DROP_OFF"` | `"PICK_UP"`
- **Logistic Order Status**: `"SUBMITTED"` | `"PROCESSING"` | `"COMPLETED"` | `"CANCELLED"`
- **Mission Type**: `"DROP_OFF_WEIGHT"` | `"TRANSACTION_COUNT"` | `"DAILY_LOGIN"` | `"RECYCLING_STREAK"`
- **Mission Progress Status**: `"IN_PROGRESS"` | `"COMPLETED"` | `"CLAIMED"`
- **User Voucher Status**: `"AVAILABLE"` | `"USED"` | `"EXPIRED"`
- **Exchange Transaction Status**: `"PENDING"` | `"SUCCESS"` | `"FAILED"`
