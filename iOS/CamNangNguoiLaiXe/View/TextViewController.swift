//
//  TextViewController.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/12/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit

class TextViewController: BaseViewController {
    
    var item: content?
    
    var scrollView: UIScrollView = {
        let sc = UIScrollView(frame: CGRect.zero)
        sc.backgroundColor = .clear
        sc.showsVerticalScrollIndicator = false
        return sc
    }()
    
    override func viewDidLoad() {
        page = "MultiView"
        titlePage = item?.title
        super.viewDidLoad()
        checkRotated()
        print ("Scene : TextViewController")
        updateUI()
//        self.view.backgroundColor = UIColor(hexString: "D8D8D8")
        self.view.backgroundColor = .white
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
        let name = UILabel(frame: CGRect(x: 0, y: height, w: scrollView.w, h: 30))
        name.numberOfLines = 0
        name.textAlignment = .center
        name.backgroundColor = .white
        name.font = UIFont.boldSystemFont(ofSize: 20)
        name.text = item?.title
        name.frame = CGRect(x: 0, y: height, w: scrollView.w, h: name.getEstimatedHeight() + 20)
        scrollView.addSubview(name)
        height = name.h + name.y
        if (item?.image.length)! > 0 {
            let image = UIImage(named: (item?.image.lowercased())! + ".png")
            var _height: CGFloat = 150
            if let img = image {
                _height = scrollView.w * (img.size.height) / (img.size.width)
            }
            let img = UIImageView(frame: CGRect(x: 0, y: height + 5, w: scrollView.w, h: _height))
//            img.backgroundColor = .clear
            img.backgroundColor = .white
            img.image = image
            scrollView.addSubview(img)
            
            height = img.h + img.y + 5
        }
        if (item?.detail.length)! > 0 {
            let des = UITextView(frame: CGRect(x: 2, y: height + 5, w: scrollView.w - 4, h: 100))
//            des.backgroundColor = .clear
            des.backgroundColor = .white
            des.font = UIFont.systemFont(ofSize: 20)
            des.textColor = UIColor(hexString: "585858")
            des.isEditable = false
            des.isSelectable = false
            des.isScrollEnabled = false
            let htmlString = "<html>" +
                "<head>" +
                "<style>" +
                "body {" +
                "font-size: 19;" +
                "font-family: 'Arial';" +
                "text-decoration:none;" +
                "}" +
                "table {" +
                "font-size: 18; " +
                "font-family: 'Arial';" +
                "text-decoration:none;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" + "\((item?.detail)!)" + "</body></head></html>"
            do {
                let str = try NSMutableAttributedString(data: (htmlString.data(using: String.Encoding.unicode, allowLossyConversion: true)!), options: [NSDocumentTypeDocumentAttribute : NSHTMLTextDocumentType], documentAttributes: nil)
                des.attributedText = str
            } catch {
                print(error)
            }
            let fixedWidth = des.frame.size.width
            des.sizeThatFits(CGSize(width: fixedWidth, height: CGFloat.greatestFiniteMagnitude))
            let newSize = des.sizeThatFits(CGSize(width: fixedWidth, height: CGFloat.greatestFiniteMagnitude))
            var newFrame = des.frame
            newFrame.size = CGSize(width: max(newSize.width, fixedWidth), height: newSize.height)
            des.frame = newFrame;
//            des.frame = CGRect(x: des.x, y: des.y, w: des.w, h: des.getEstimatedHeight())
            
            // Iphone X Not Show Large TextUI
            if UIDevice().userInterfaceIdiom == .phone {
                switch UIScreen.main.nativeBounds.height {
                case 2436:
                    des.isScrollEnabled = true
                default:
                    print("unknown")
                }
            }
            
            scrollView.addSubview(des)
            
            height = des.h + 5 + des.y
        }
        
        scrollView.contentSize = CGSize(width: scrollView.w, height: height + 50)
        setupAds()
    }
    
    override func renderLandscape () {
        setup ()
    }
    override func onAfterLoadHeader () {
        btnBack?.isHidden = false
        btnTop?.isHidden = false
    }
    override func renderPortrait () {
        setup ()
    }
}
