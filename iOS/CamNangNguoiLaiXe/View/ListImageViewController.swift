//
//  ListImageViewController.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/12/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit

class ListImageViewController: BaseViewController {
    
    var model: smallTopic?
    
    var scrollView: UIScrollView = {
        let sc = UIScrollView(frame: CGRect.zero)
        sc.backgroundColor = .clear
        sc.showsVerticalScrollIndicator = false
        return sc
    }()
    
    override func viewDidLoad() {
        page = "ListImage"
        titlePage = model?.title
        super.viewDidLoad()
        checkRotated()
        updateUI()
        self.view.backgroundColor = UIColor(hexString: "D8D8D8")
//        setup()
    }
    
    override func actionScrollTop() {
        scrollView.setContentOffset(CGPoint.zero, animated: true)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - setup
    func setup() {
        scrollView.removeSubviews()
        scrollView.frame = CGRect(x: 0, y: (viewHeader?.h)!, w: self.view.w, h: self.view.h - ((viewHeader?.h)!))
        self.view.addSubview(scrollView)
        
        var height: CGFloat = 0
        for item in (model?.content!)! {
            let vi = UIView(frame: CGRect(x: 0, y: height, w: scrollView.w, h: 20))
            vi.backgroundColor = .white
            scrollView.addSubview(vi)
            
            let img = UIImageView(frame: CGRect(x: 10, y: 10, w: 150, h: 150))
            img.image = UIImage(named: item.image.lowercased())
            vi.addSubview(img)
            
            let name = UILabel(frame: CGRect(x: img.x + img.w + 5, y: 10, w: vi.w - (img.x + img.w + 5 + 10), h: 30))
            name.numberOfLines = 0
            name.textColor = UIColor(hexString: "585858")
            name.font = UIFont.systemFont(ofSize: 18)
            name.text = item.title
            name.frame = CGRect(x: name.x, y: name.y, w: name.w, h: name.getEstimatedHeight() + 20)
            vi.addSubview(name)
            vi.frame = CGRect(x: vi.x, y: vi.y, w: vi.w, h: img.y + img.h + 20)
            height = vi.h + vi.y + 1
            vi.addTapGesture(action: { (tap) in
                vi.alpha = 0.5
                UIView.animate(withDuration: 0.25, animations: { 
                    vi.alpha = 1
                    self.popupView(item)
                })
            })
        }
        scrollView.contentSize = CGSize(width: scrollView.w, height: height + 50)
        setupAds()
    }
    
    func popupView(_ content: content) {
        let overlay = UIView(frame: self.view.bounds)
        overlay.backgroundColor = UIColor(white: 0.0, alpha: 0.5)
        self.view.addSubview(overlay)
        
        let vi = UIView(frame: CGRect(x: 20, y: 20, w: self.view.w - 40, h: self.view.h - 40))
        vi.backgroundColor = .white
        overlay.addSubview(vi)
        
        let img = UIImageView(frame: CGRect(x: 10, y: 10, w: 100, h: 100))
        img.image = UIImage(named: content.image.lowercased())
        vi.addSubview(img)
        
        let name = UILabel(frame: CGRect(x: img.x + img.w + 5, y: 0, w: vi.w - (img.x + img.w + 5 + 10), h: 20))
        name.numberOfLines = 0
//        name.textColor = UIColor(hexString: "585858")
        name.font = UIFont.systemFont(ofSize: 18)
        name.text = content.title
        name.frame = CGRect(x: name.x, y: name.y, w: name.w, h: name.getEstimatedHeight() + 20)
        vi.addSubview(name)
        
        var sc_y: CGFloat = 0
        if (img.h >= name.h) {
            sc_y = img.h + img.y
        }
        else {
            sc_y = name.h + name.y
        }
        let sc = UIScrollView(frame: CGRect(x: 20, y: sc_y + 15, w: vi.w - 40, h: vi.h - (sc_y + 5) - 55))
        sc.backgroundColor = .clear
        vi.addSubview(sc)
        let des = UILabel(frame: CGRect(x: 0, y: 0, w: sc.w, h: 100))
        des.backgroundColor = .clear
        des.numberOfLines = 0
        des.textColor = UIColor(hexString: "585858")
        des.font = UIFont.systemFont(ofSize: 18)
        des.text = content.detail.html2String
        des.frame = CGRect(x: des.x, y: des.y, w: des.w, h: des.getEstimatedHeight())
        sc.addSubview(des)
        sc.contentSize = CGSize(width: sc.w, height: des.y + des.h + 5)
        
        let btn = UIButton(type: .custom)
        btn.frame = CGRect(x: vi.w - 60, y: sc.y + sc.h, w: 50, h: 44)
        btn.setTitle("Close", for: .normal)
        btn.setTitleColor(.black, for: .normal)
        vi.addSubview(btn)
        btn.addTapGesture { (tap) in
            UIView.animate(withDuration: 0.25, animations: {
                overlay.removeFromSuperview()
            })
        }
    }
    
    func getDocumentsURL() -> NSURL {
        let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
        return documentsURL as NSURL
    }
    
    func fileInDocumentsDirectory(filename: String) -> String {
        
        let fileURL = getDocumentsURL().appendingPathComponent(filename)
        return fileURL!.path
    }
    
    func loadImageFromPath(path: String) -> UIImage? {
        
        let image = UIImage(contentsOfFile: path)
        
        if image == nil {
            
            print("missing image at: \(path)")
        }
        print("Loading image from path: \(path)") // this is just for you to see the path in case you want to go to the directory, using Finder.
        return image
        
    }
    
    
    override func onAfterLoadHeader () {
        btnBack?.isHidden = false
        btnTop?.isHidden = false
    }
    
    override func renderLandscape () {
        setup ()
    }
    
    override func renderPortrait () {
        setup ()
    }
    
}
