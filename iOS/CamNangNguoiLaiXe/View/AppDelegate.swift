//
//  AppDelegate.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/4/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit
import GoogleMobileAds
import Siren
import FacebookCore
import FBSDKCoreKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?


    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        SDKApplicationDelegate.shared.application(application, didFinishLaunchingWithOptions: launchOptions)
        
        return true
    }
    
    func application(_ application: UIApplication, open url: URL, sourceApplication: String?, annotation: Any) -> Bool {
        return SDKApplicationDelegate.shared.application(application,
                                                         open: url,
                                                         sourceApplication: sourceApplication,
                                                         annotation: annotation)
    }
    
    @available(iOS 9.0, *)
    func application(_ application: UIApplication,
                     open url: URL,
                     options: [UIApplicationOpenURLOptionsKey: Any]) -> Bool {
        // Override point for customization after application launch.
        GADMobileAds.configure(withApplicationID: "ca-app-pub-3669761949184169~2497463938")
        print("TayPVS - Application")
        copyDatabase()
        self.window?.makeKeyAndVisible()
        // Siren is a singleton
        let siren = Siren.shared
        /*
         Replace .Immediately with .Daily or .Weekly to specify a maximum daily or weekly frequency for version
         checks.
         */
        siren.checkVersion(checkType: .immediately)
        siren.alertType = .force
        
        NotificationCenter.default.addObserver(self, selector: #selector(AppDelegate.rotated), name: NSNotification.Name.UIDeviceOrientationDidChange, object: nil)

        return FBSDKApplicationDelegate.sharedInstance().application(application, open: url, options: options)
        
        
    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
        Siren.shared.checkVersion(checkType: .immediately)
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        AppEventsLogger.activate(application)
        Siren.shared.checkVersion(checkType: .daily)
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }

    func rotated() {
        if UIDeviceOrientationIsLandscape(UIDevice.current.orientation) {
//            print("Landscape")
        }
        
        if UIDeviceOrientationIsPortrait(UIDevice.current.orientation) {
//            print("Portrait")
        }
        
    }
    
    func copyDatabase(){
        print("TayPVS - copyDatabase")
        let fileManager = FileManager.default
        let dbPath = getDBPath()
        let success = fileManager.fileExists(atPath: dbPath)
        if(!success) {
            if let defaultDBPath = Bundle.main.path(forResource: "init", ofType: "json"){
                do {
                    try fileManager.copyItem(atPath: defaultDBPath, toPath: dbPath)
                    print("TayPVS - success")
                }
                catch let error as NSError {
                    print("TayPVS - Ooops! Something went wrong: \(error)")
                }
            }else{
                print("TayPVS - Cannot Find File In NSBundle")
            }
        }else{
            print("TayPVS - File Already Exist At:\(dbPath)")
        }
    }
    
    
    func getDBPath()->String
    {
        let paths = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.userDomainMask, true)
        let documentsDir = paths[0] 
        let databasePath = documentsDir + "/init.json"
        return databasePath;
    }

}

