//
//  BaseViewController.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/4/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit
import GoogleMobileAds

class BaseViewController: UIViewController {

    var viewHeader: UIView?
    var lblTitleHeader: UILabel?
    var btnBack: UIButton?
    var btnTop: UIButton?
    var page : String?
    var titlePage : String?
    var currentOrientation : String?
    var isLoadAds : Bool? = false
    
    lazy var adBannerView: GADBannerView = {
        let adBannerView = GADBannerView(adSize: kGADAdSizeSmartBannerPortrait)
        adBannerView.adUnitID = "ca-app-pub-3669761949184169/9242743131"
        adBannerView.rootViewController = self
        
        return adBannerView
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        print("TayPVS ADD OBSER")
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.checkRotated), name: NSNotification.Name.UIDeviceOrientationDidChange, object: nil)
        
        
        // Request a Google Ad
        
//        adBannerView.delegate = self
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
        if viewHeader != nil {
//            checkRotated()
        }
    }
    
    func setupAds() {
        if isLoadAds == false{
            let navigationBarHeight: CGFloat = self.navigationController!.navigationBar.frame.height
            adBannerView.frame = CGRect(x: 0, y: self.view.h - 50 - navigationBarHeight, w: self.view.w, h: 50 + navigationBarHeight)
            self.view.insertSubview(adBannerView, at: 10)
            adBannerView.load(GADRequest())
            isLoadAds = true
        }
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setupHeader(pageTitle : String) {
        
        let navigationBarHeight: CGFloat = self.navigationController!.navigationBar.frame.height
        
        self.navigationController?.isNavigationBarHidden = true
        self.view.backgroundColor = UIColor(hexString: "ECF1F2")
        viewHeader?.removeSubviews()
        viewHeader?.removeFromSuperview()
        viewHeader = UIView(frame: CGRect(x: 0, y: 0, w: self.view.w, h: 64 + navigationBarHeight))
//        viewHeader?.backgroundColor = UIColor(hexString: "262626")
        viewHeader?.backgroundColor = UIColor(hexString: "FFD217")
        self.view.addSubview(viewHeader!)
        
        lblTitleHeader = UILabel(frame: CGRect(x: 40, y: navigationBarHeight, w: (viewHeader?.w)! - 80, h: (viewHeader?.h)!/2 - 15))
        lblTitleHeader?.centerY = (viewHeader?.h)! / 2 + 9
        lblTitleHeader?.textAlignment = .center
        lblTitleHeader?.numberOfLines = 0
        lblTitleHeader?.textColor = .white
        lblTitleHeader?.font = UIFont.systemFont(ofSize: 20)
        lblTitleHeader?.text = pageTitle
        viewHeader?.addSubview(lblTitleHeader!)
        
        // Update Header Height
        viewHeader?.frame = CGRect(x: 0, y: 0, w: self.view.w, h: (lblTitleHeader?.y)! + (lblTitleHeader?.getEstimatedHeight())! + 20)
        
        btnBack = UIButton(type: .custom)
        btnBack?.frame = CGRect(x: 10, y: navigationBarHeight, w: 25, h: 25)
        btnBack?.centerY = (viewHeader?.h)! / 2 + 9
        btnBack?.setImage(#imageLiteral(resourceName: "icon_arrow_left"), for: .normal)
        viewHeader?.addSubview(btnBack!)
        btnBack?.isHidden = true
        btnBack?.addTarget(self, action: #selector(goBack), for: .touchUpInside)
        
        btnTop = UIButton(type: .custom)
        btnTop?.frame = CGRect(x: (viewHeader?.w)! - 25 - 10, y: navigationBarHeight, w: 25, h: 25)
        btnTop?.centerY = (viewHeader?.h)! / 2 + 9
        btnTop?.setImage(#imageLiteral(resourceName: "icon_arrow_up"), for: .normal)
        viewHeader?.addSubview(btnTop!)
        btnTop?.isHidden = true
        btnTop?.addTarget(self, action: #selector(actionScrollTop), for: .touchUpInside)
        onAfterLoadHeader();
    }
    
    func setupHeaderHome() {
         print("TAYPVS >>>>>>>>> setupHeaderHome >>>>>>>><<<<<<<<<<<<<<")
        let navigationBarHeight: CGFloat = self.navigationController!.navigationBar.frame.height
        
        self.navigationController?.isNavigationBarHidden = true
        self.view.backgroundColor = .white
        viewHeader?.removeSubviews()
        viewHeader?.removeFromSuperview()
        viewHeader = UIView(frame: CGRect(x: 0, y: 0, w: self.view.w, h: 154 + navigationBarHeight))
//        viewHeader?.isHidden = true
        viewHeader?.backgroundColor = UIColor(hexString: "FFD217")
        self.view.addSubview(viewHeader!)
        
        let logo_height: CGFloat = 140
        
        let img = UIImageView(frame: CGRect(x: 10, y: (viewHeader?.h)! - logo_height - 20 , w: logo_height, h: logo_height))
        img.image = UIImage(named: "map")
        img.contentMode = .scaleAspectFit
        viewHeader?.addSubview(img)
        
        let cammang_height: CGFloat = 60
        let cammang_width: CGFloat = (viewHeader?.w)! - img.w - 20
        
        
        let img_CamNang = UIImageView(frame: CGRect(x: img.w, y: (viewHeader?.h)!/2 - 45 , w: cammang_width, h: cammang_height))
        img_CamNang.image = UIImage(named: "camnang_text")
        img_CamNang.contentMode = .scaleAspectFit
        viewHeader?.addSubview(img_CamNang)
      
        let text_antoan = UITextView(frame: CGRect(x: img.w + 15, y: (viewHeader?.h)!/2 + 25 , w: cammang_width, h: logo_height))
        text_antoan.text = "Lái xe an toàn,\n \u{00a0}\u{00a0}\u{00a0}về nhà hạnh phúc!"
        text_antoan.font = UIFont.italicSystemFont(ofSize: 20)
        text_antoan.isUserInteractionEnabled = false
        
        text_antoan.backgroundColor = .clear
        viewHeader?.addSubview(text_antoan)
        
//        let title_x: CGFloat = img.x + img.w + 20
//        lblTitleHeader = UILabel(frame: CGRect(x: title_x , y: navigationBarHeight, w: (viewHeader?.w)! - title_x, h: 55))
//        lblTitleHeader?.centerY = (viewHeader?.h)! / 2 + 9
//        lblTitleHeader?.textAlignment = .center
//        lblTitleHeader?.numberOfLines = 2
//        lblTitleHeader?.textColor = .white
//
//        viewHeader?.addSubview(lblTitleHeader!)
        
    }
    
    func onAfterLoadHeader () {
    
    }
    
    func checkRotated() {
        print("TAYPVS >>>>>>>>> checkRotated >>>>>>>><<<<<<<<<<<<<<")
        
        if page == "Home"{
            setupHeaderHome()
        } else {
            setupHeader(pageTitle: titlePage!)
        }
        
        rerenderAds ()
        if UIDevice.current.orientation.isLandscape {
            if (currentOrientation != "LandScape"){
                currentOrientation = "LandScape"
                renderLandscape ()
            }
        } else {
            if (currentOrientation != "Potrait") {
                currentOrientation = "Potrait"
                renderPortrait ()
            }
        }
    }
    
    func renderLandscape () {
    
    }
    
    func renderPortrait () {
        
    }
    
    func rerenderHeader () {
        print("Render Header")
        let navigationBarHeight: CGFloat = self.navigationController!.navigationBar.frame.height
        
        self.navigationController?.isNavigationBarHidden = true
        viewHeader?.frame = CGRect(x: 0, y: 0, w: self.view.w, h: (lblTitleHeader?.y)! + (lblTitleHeader?.getEstimatedHeight())! + 20)
        
        self.view.addSubview(viewHeader!)
        
        lblTitleHeader?.frame = CGRect(x: 40, y: navigationBarHeight + 4, w: (viewHeader?.w)! - 80, h: (viewHeader?.h)!/2 - 15)
        
        lblTitleHeader?.numberOfLines = 0
        lblTitleHeader?.textColor = .white
        lblTitleHeader?.font = UIFont.systemFont(ofSize: 20)

        btnTop?.frame = CGRect(x: (viewHeader?.w)! - 25 - 10, y: (btnTop?.y)!, w: 25, h: 25)

        viewHeader?.addSubview(lblTitleHeader!)
        rerenderAds ()
    }
    
    func rerenderAds () {
        let navigationBarHeight: CGFloat = self.navigationController!.navigationBar.frame.height
        adBannerView.frame = CGRect(x: 0, y: self.view.h - 50 - navigationBarHeight, w: self.view.w, h: 50 + navigationBarHeight)
    }
    
    func updateUI() {
//        lblTitleHeader?.frame = CGRect(x: (lblTitleHeader?.x)!, y: (lblTitleHeader?.y)!, w: (lblTitleHeader?.w)!, h: (lblTitleHeader?.getEstimatedHeight())!)
//        viewHeader?.frame = CGRect(x: (viewHeader?.x)!, y: (viewHeader?.y)!, w: self.view.w, h: (lblTitleHeader?.y)! + (lblTitleHeader?.getEstimatedHeight())! + 10)
//        
    }
    
    @objc func goBack() {
        self.navigationController?.popViewController(animated: true)
    }
    
    func actionScrollTop() {
        
    }
    
    func visibleHeader () {
        viewHeader?.isHidden = false
    }

}
